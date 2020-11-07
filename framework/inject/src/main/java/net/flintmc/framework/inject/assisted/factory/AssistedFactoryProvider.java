package net.flintmc.framework.inject.assisted.factory;

import com.google.common.collect.*;
import com.google.inject.*;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.spi.*;
import com.google.inject.util.Providers;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.assisted.ConstructorMatcher;
import net.flintmc.framework.inject.assisted.binding.BindingCollector;
import net.flintmc.framework.inject.assisted.data.AssistData;
import net.flintmc.framework.inject.assisted.data.AssistedInjectBinding;
import net.flintmc.framework.inject.assisted.data.AssistedInjectTargetVisitor;
import net.flintmc.framework.inject.assisted.data.AssistedMethod;
import net.flintmc.framework.inject.assisted.thread.ThreadLocalProvider;
import net.flintmc.framework.inject.primitive.InjectionHolder;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.getOnlyElement;

public class AssistedFactoryProvider<F> implements HasDependencies, ProviderWithExtensionVisitor<F>, AssistedInjectBinding<F>, InvocationHandler {

  // Initializes a default annotation.
  private static final Assisted DEFAULT_ANNOTATION = new Assisted() {
    @Override
    public Class<? extends Annotation> annotationType() {
      return Assisted.class;
    }

    @Override
    public String value() {
      return "";
    }
  };

  private static final Annotation RETURN_ANNOTATION = UniqueAnnotations.create();

  private final ImmutableMap<Method, AssistData> assistDatas;
  private final ImmutableMap<Method, MethodHandle> methodHandles;
  private final Key<F> factoryKey;
  private final F factory;

  private Injector injector;

  /**
   * @param factoryKey A key for a Java interface that defines one or more create methods.
   * @param collector  Binding configuration that maps method return types to implementation types.
   */
  public AssistedFactoryProvider(Key<F> factoryKey, BindingCollector collector) {
    this.factoryKey = factoryKey;

    AssistedFactoryMethodHandle assistedFactoryMethodHandle = new AssistedFactoryMethodHandle();
    ConstructorMatcher constructorMatcher = new ConstructorMatcher();

    TypeLiteral<F> factoryType = factoryKey.getTypeLiteral();
    Errors errors = new Errors();

    @SuppressWarnings("unchecked")
    Class<F> factoryRawType = (Class<F>) factoryType.getRawType();

    try {
      if (!factoryRawType.isInterface()) {
        throw errors.addMessage("%s must be an interface.", factoryRawType).toException();
      }

      Multimap<String, Method> defaultMethods = HashMultimap.create();
      Multimap<String, Method> otherMethods = HashMultimap.create();
      ImmutableMap.Builder<Method, AssistData> assistDataBuilder = ImmutableMap.builder();

      for (Method method : factoryRawType.getMethods()) {

        if (Modifier.isStatic(method.getModifiers())) {
          continue;
        }

        if (this.isDefault(method) && (method.isBridge() || method.isSynthetic())) {

          validateFactoryReturnType(errors, method.getReturnType(), factoryRawType);
          defaultMethods.put(method.getName(), method);
          continue;
        }
        otherMethods.put(method.getName(), method);

        TypeLiteral<?> returnTypeLiteral = factoryType.getReturnType(method);
        Key<?> returnType;
        try {
          returnType =
                  Annotations.getKey(returnTypeLiteral, method, method.getAnnotations(), errors);
        } catch (ConfigurationException configurationException) {
          if (isTypeNotSpecified(returnTypeLiteral, configurationException)) {
            throw errors.keyNotFullySpecified(TypeLiteral.get(factoryRawType)).toException();
          } else {
            throw configurationException;
          }
        }

        // Validates the factory return type
        validateFactoryReturnType(errors, returnType.getTypeLiteral().getRawType(), factoryRawType);

        List<TypeLiteral<?>> parameters = factoryType.getParameterTypes(method);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        int providerCount = 0;
        List<Key<?>> keys = Lists.newArrayList();

        for (TypeLiteral<?> parameter : parameters) {

          Key<?> parameterKey = Annotations.getKey(parameter, method, parameterAnnotations[providerCount++], errors);
          Class<?> underlylingType = parameterKey.getTypeLiteral().getRawType();

          if (underlylingType.equals(Provider.class) || underlylingType.equals(javax.inject.Provider.class)) {
            errors.addMessage(
                    "A Provider may not be a type in a factory method of an AssistedInject."
                            + "\n  Offending instance is parameter [%s] with key [%s] on method [%s]",
                    providerCount, parameterKey, method);
          }

          keys.add(assistKey(method, parameterKey, errors));
        }

        ImmutableList<Key<?>> immutableParamList = ImmutableList.copyOf(keys);

        TypeLiteral<?> implementation = collector.getBindings().get(returnType);
        if (implementation == null) {
          implementation = returnType.getTypeLiteral();
        }

        Class<? extends Annotation> scope = Annotations.findScopeAnnotation(errors, implementation.getRawType());
        if (scope != null) {
          errors.addMessage(
                  "Found scope annotation [%s] on implementation class "
                          + "[%s] of AssistedInject factory [%s].\nThis is not allowed, please"
                          + " remove the scope annotation.",
                  scope, implementation.getRawType(), factoryType);
        }

        InjectionPoint constructorInjectionPoint;
        try {
          constructorInjectionPoint = constructorMatcher.findMatchingConstructorInjectionPoint(
                  method,
                  returnType,
                  implementation,
                  immutableParamList);
        } catch (ErrorsException errorsException) {
          errors.merge(errorsException.getErrors());
          continue;
        }

        Constructor<?> constructor = (Constructor<?>) constructorInjectionPoint.getMember();
        List<ThreadLocalProvider> providers = Collections.emptyList();
        Set<Dependency<?>> dependencies = getDependencies(constructorInjectionPoint, implementation);

        boolean optimized = false;

        if (isValidForOptimizedAssistedInject(dependencies)) {
          ImmutableList.Builder<ThreadLocalProvider> providerListBuilder = ImmutableList.builder();
          for (int i = 0; i < parameters.size(); i++) {
            providerListBuilder.add(new ThreadLocalProvider());
          }
          providers = providerListBuilder.build();
          optimized = true;
        }

        AssistData data =
                new AssistData(
                        constructor,
                        returnType,
                        immutableParamList,
                        implementation,
                        method,
                        removeAssistedDependencies(dependencies),
                        optimized,
                        providers);
        assistDataBuilder.put(method, data);
      }

      this.factory =
              factoryRawType.cast(Proxy.newProxyInstance(
                      factoryRawType.getClassLoader(),
                      new Class[]{factoryRawType},
                      this
              ));

      Map<Method, AssistData> dataSoFactory = assistDataBuilder.build();
      ImmutableMap.Builder<Method, MethodHandle> methodHandleBuilder = ImmutableMap.builder();

      for (Map.Entry<String, Method> entry : defaultMethods.entries()) {
        Method defaultMethod = entry.getValue();
        MethodHandle handle = assistedFactoryMethodHandle.createMethodHandle(defaultMethod, this.factory);
        if (handle != null) {
          methodHandleBuilder.put(defaultMethod, handle);
        } else {
          boolean foundMatch = false;
          for (Method otherMethod : otherMethods.get(defaultMethod.getName())) {
            if (dataSoFactory.containsKey(otherMethod) && isCompatible(defaultMethod, otherMethod)) {
              if (foundMatch) {
                errors.addMessage(
                        "Generated default method %s with parameters %s is"
                                + " signature-compatible with more than one non-default method."
                                + " Unable to create factory. As a workaround, remove the override"
                                + " so javac stops generating a default method.",
                        defaultMethod, Arrays.asList(defaultMethod.getParameterTypes()));
              } else {
                assistDataBuilder.put(defaultMethod, dataSoFactory.get(otherMethod));
                foundMatch = true;
              }
            }
          }
          if (!foundMatch) {
            throw new IllegalStateException("Can't find method compatible with: " + defaultMethod);
          }
        }
      }

      if (errors.hasErrors()) {
        throw errors.toException();
      }

      this.assistDatas = assistDataBuilder.build();
      this.methodHandles = methodHandleBuilder.build();
    } catch (ErrorsException exception) {
      throw new ConfigurationException(exception.getErrors().getMessages());
    }
  }

  private boolean isDefault(Method method) {
    return (method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC;
  }

  private boolean isCompatible(Method source, Method destination) {
    if (!source.getReturnType().isAssignableFrom(destination.getReturnType())) {
      return false;
    }
    Class<?>[] sourceParameters = source.getParameterTypes();
    Class<?>[] destinationParameters = destination.getParameterTypes();
    return sourceParameters.length == destinationParameters.length &&
            IntStream.range(0, sourceParameters.length)
                    .allMatch(i -> sourceParameters[i].isAssignableFrom(destinationParameters[i]));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public F get() {
    return factory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Dependency<?>> getDependencies() {
    Set<Dependency<?>> combinedDeps = new HashSet<>();
    for (AssistData data : assistDatas.values()) {
      combinedDeps.addAll(data.getDependencies());
    }
    return ImmutableSet.copyOf(combinedDeps);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Key<F> getKey() {
    return factoryKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public Collection<AssistedMethod> getAssistedMethods() {
    return (Collection<AssistedMethod>) (Collection<?>) assistDatas.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T, V> V acceptExtensionVisitor(
          BindingTargetVisitor<T, V> visitor, ProviderInstanceBinding<? extends T> binding) {
    if (visitor instanceof AssistedInjectTargetVisitor) {
      return ((AssistedInjectTargetVisitor<T, V>) visitor).visit((AssistedInjectBinding<T>) this);
    }
    return visitor.visit(binding);
  }

  private void validateFactoryReturnType(Errors errors, Class<?> returnType, Class<?> factoryType) {
    if (Modifier.isPublic(factoryType.getModifiers()) && !Modifier.isPublic(returnType.getModifiers())) {
      errors.addMessage(
              "%s is public, but has a method that returns a non-public type: %s. "
                      + "Due to limitations with java.lang.reflect.Proxy, this is not allowed. "
                      + "Please either make the factory non-public or the return type public.",
              factoryType, returnType);
    }
  }

  /**
   * @return {@code true} if the {@link ConfigurationException} is due to an error of {@link TypeLiteral} not being
   * fully specified.
   */
  private boolean isTypeNotSpecified(TypeLiteral<?> typeLiteral, ConfigurationException configurationException) {
    Collection<Message> messages = configurationException.getErrorMessages();
    if (messages.size() == 1) {
      Message msg =
              Iterables.getOnlyElement(new Errors().keyNotFullySpecified(typeLiteral).getMessages());
      return msg.getMessage().equals(Iterables.getOnlyElement(messages).getMessage());
    } else {
      return false;
    }
  }

  /**
   * Calculates all dependencies required by the implementation and constructor.
   *
   * @return All calculates dependencies required by the implementation and constructor.
   */
  private Set<Dependency<?>> getDependencies(
          InjectionPoint ctorPoint, TypeLiteral<?> implementation) {
    ImmutableSet.Builder<Dependency<?>> builder = ImmutableSet.builder();
    builder.addAll(ctorPoint.getDependencies());
    if (!implementation.getRawType().isInterface()) {
      for (InjectionPoint ip : InjectionPoint.forInstanceMethodsAndFields(implementation)) {
        builder.addAll(ip.getDependencies());
      }
    }
    return builder.build();
  }

  /**
   * Retrieves all non-assisted dependencies.
   *
   * @return All non-assisted dependencies.
   */
  private Set<Dependency<?>> removeAssistedDependencies(Set<Dependency<?>> deps) {
    ImmutableSet.Builder<Dependency<?>> builder = ImmutableSet.builder();
    for (Dependency<?> dep : deps) {
      Class<?> annotationType = dep.getKey().getAnnotationType();
      if (annotationType == null || !annotationType.equals(Assisted.class)) {
        builder.add(dep);
      }
    }
    return builder.build();
  }

  /**
   * @return {@code true} if all dependencies are suitable for the optimized version of {@link AssistedInject}. The
   * optimized version caches the binding & uses a {@link ThreadLocalProvider}, so can only be applied if
   * the assisted bindings are immediately provided. This looks for hints that the values may be
   * lazily retrieved, by looking for injections of Injector or a Provider for the assisted values.
   */
  private boolean isValidForOptimizedAssistedInject(Set<Dependency<?>> dependencies) {
    Set<Dependency<?>> badDeps = null; // optimization: create lazily
    for (Dependency<?> dep : dependencies) {
      if (isInjectorOrAssistedProvider(dep)) {
        if (badDeps == null) {
          badDeps = Sets.newHashSet();
        }
        badDeps.add(dep);
      }
    }
    return badDeps == null || badDeps.isEmpty();
  }

  /**
   * Returns true if the dependency is for {@link Injector} or if the dependency is a {@link
   * Provider} for a parameter that is {@literal @}{@link Assisted}.
   */
  private boolean isInjectorOrAssistedProvider(Dependency<?> dependency) {
    Class<?> annotationType = dependency.getKey().getAnnotationType();

    if (annotationType != null && annotationType.equals(Assisted.class)) {
      return dependency
              .getKey()
              .getTypeLiteral()
              .getRawType()
              .equals(Provider.class);
    }

    return dependency
            .getKey()
            .getTypeLiteral()
            .getRawType()
            .equals(Injector.class);
  }

  /**
   * @return A key similar to {@code key}, but with an {@link Assisted} binding annotation. This
   * fails if another binding annotation is clobbered in the process. If the key already has the
   * {@link Assisted} annotation, it is returned as-is to preserve any String value.
   */
  private <T> Key<T> assistKey(Method method, Key<T> key, Errors errors) throws ErrorsException {
    if (key.getAnnotationType() == null) {
      return Key.get(key.getTypeLiteral(), DEFAULT_ANNOTATION);
    } else if (key.getAnnotationType() == Assisted.class) {
      return key;
    } else {
      errors
              .withSource(method)
              .addMessage(
                      "Only @Assisted is allowed for factory parameters, but found @%s",
                      key.getAnnotationType());
      throw errors.toException();
    }
  }


  /**
   * At injector-creation time, we initialize the invocation handler. At this time we make sure all factory
   * methods will be able to build the target types.
   *
   * @param injector The injector.
   */
  @Inject
  @Toolable
  private void initialize(Injector injector) {

    if (this.injector != null) {
      throw new ConfigurationException(
              ImmutableList.of(
                      new Message(
                              AssistedFactoryProvider.class,
                              "Factories.create() factories may only be used in one Injector!"
                      )
              )
      );
    }

    this.injector = injector;

    for (Map.Entry<Method, AssistData> entry : this.assistDatas.entrySet()) {
      Method method = entry.getKey();
      AssistData data = entry.getValue();

      Object[] arguments;
      if (!data.isOptimized()) {
        arguments = new Object[method.getParameterTypes().length];
        Arrays.fill(arguments, "dummy object for validating Factories");
      } else {
        arguments = null;
      }

      this.getBindingFromNewInjector(method, arguments, data);
    }

  }

  /**
   * Creates a child injector that binds the {@code arguments}, and returns the binding for the {@code method}'s result.
   *
   * @param method    The method, which should be binding.
   * @param arguments The arguments, which should be binding.
   * @param data      The assisted data.
   * @return The binding for the method's result.
   */
  public Binding<?> getBindingFromNewInjector(Method method, Object[] arguments, AssistData data) {
    checkState(this.injector != null, "Factories.create() factories cannot be used until they're initialized by Guice.");

    final Key<?> returnType = data.getReturnType();
    final Key<?> returnKey = Key.get(returnType.getTypeLiteral(), RETURN_ANNOTATION);

    Module assistedModule = new AbstractModule() {
      @Override
      @SuppressWarnings({
              "unchecked",
              "rawtypes"
      })
      protected void configure() {
        Binder binder = binder().withSource(method);

        int providerCount = 0;
        if (!data.isOptimized()) {
          for (Key<?> paramKey : data.getParamTypes()) {
            binder.bind((Key) paramKey).toProvider(Providers.of(arguments[providerCount++]));
          }
        } else {
          for (Key<?> paramKey : data.getParamTypes()) {
            binder.bind((Key) paramKey).toProvider(data.getProviders().get(providerCount++));
          }
        }

        Constructor constructor = data.getConstructor();

        if (constructor != null) {
          binder
                  .bind(returnKey)
                  .toConstructor(constructor, (TypeLiteral) data.getImplementationType())
                  .in(Scopes.NO_SCOPE); // make sure we erase any scope on the implementation type
        }
      }
    };

    Injector forCreate = injector.createChildInjector(assistedModule);
    Binding<?> binding = forCreate.getBinding(returnKey);

    if (data.isOptimized()) {
      data.setCachedBinding(binding);
    }
    return binding;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (this.methodHandles.containsKey(method)) {
      return this.methodHandles.get(method).invokeWithArguments(args);
    }

    if (method.getDeclaringClass().equals(Object.class)) {
      if ("equals".equals(method.getName())) {
        return proxy == args[0];
      } else if ("hashCode".equals(method.getName())) {
        return System.identityHashCode(proxy);
      } else {
        return InjectionHolder.getInjectedInstance(method.invoke(this, args).getClass());
      }
    }

    AssistData data = this.assistDatas.get(method);
    checkState(data != null, "No data for method: %s", method);
    Provider<?> provider = data.getCachedBinding() != null ?
            data.getCachedBinding().getProvider() :
            getBindingFromNewInjector(
                    method,
                    args,
                    data
            ).getProvider();
    try {
      int providerCount = 0;
      for (ThreadLocalProvider threadLocalProvider : data.getProviders()) {
        threadLocalProvider.set(args[providerCount++]);
      }
      return provider.get();
    } catch (ProvisionException exception) {
      if (exception.getErrorMessages().size() == 1) {
        Message onlyError = getOnlyElement(exception.getErrorMessages());
        Throwable cause = onlyError.getCause();
        if (cause != null && canRethrow(method, cause)) {
          throw cause;
        }
      }
      throw exception;
    } finally {
      for (ThreadLocalProvider threadLocalProvider : data.getProviders()) {
        threadLocalProvider.remove();
      }
    }
  }

  private boolean canRethrow(Method invoked, Throwable thrown) {
    if (thrown instanceof Error || thrown instanceof RuntimeException) {
      return true;
    }

    for (Class<?> declared : invoked.getExceptionTypes()) {
      if (declared.isInstance(thrown)) {
        return true;
      }
    }

    return false;
  }
}
