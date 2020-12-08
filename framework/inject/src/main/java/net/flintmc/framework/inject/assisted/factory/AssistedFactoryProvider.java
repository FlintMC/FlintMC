package net.flintmc.framework.inject.assisted.factory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Message;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.getOnlyElement;

public class AssistedFactoryProvider<F>
    implements HasDependencies,
        ProviderWithExtensionVisitor<F>,
        AssistedInjectBinding<F>,
        InvocationHandler {

  // Initializes a default annotation.
  private static final Assisted DEFAULT_ANNOTATION =
      new Assisted() {
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

  private final Key<F> factoryKey;
  private final BindingCollector collector;

  private ImmutableMap<Method, AssistData> assistData;
  private ImmutableMap<Method, MethodHandle> methodHandles;
  private F factory;
  private boolean generated;

  /**
   * @param factoryKey A key for a Java interface that defines one or more create methods.
   * @param collector Binding configuration that maps method return types to implementation types.
   */
  public AssistedFactoryProvider(Key<F> factoryKey, BindingCollector collector) {
    this.factoryKey = factoryKey;
    this.collector = collector;
  }

  /**
   * Generate the assisted factory implementation when it is required, not when creating the
   * provider so that there won't be any problems with missing dependencies for the factory.
   */
  private void generate() {
    if (this.generated) {
      return;
    }
    this.generated = true;

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
          Key<?> parameterKey =
              Annotations.getKey(parameter, method, parameterAnnotations[providerCount++], errors);
          Class<?> underlyingType = parameterKey.getTypeLiteral().getRawType();

          if (underlyingType.equals(Provider.class)
              || underlyingType.equals(javax.inject.Provider.class)) {
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

        Class<? extends Annotation> scope =
            Annotations.findScopeAnnotation(errors, implementation.getRawType());
        if (scope != null) {
          errors.addMessage(
              "Found scope annotation [%s] on implementation class "
                  + "[%s] of AssistedInject factory [%s].\nThis is not allowed, please"
                  + " remove the scope annotation.",
              scope, implementation.getRawType(), factoryType);
        }

        InjectionPoint constructorInjectionPoint;
        try {
          constructorInjectionPoint =
              constructorMatcher.findMatchingConstructorInjectionPoint(
                  method, returnType, implementation, immutableParamList);
        } catch (ErrorsException errorsException) {
          errors.merge(errorsException.getErrors());
          continue;
        }

        Constructor<?> constructor = (Constructor<?>) constructorInjectionPoint.getMember();
        List<ThreadLocalProvider> providers = Collections.emptyList();
        Set<Dependency<?>> dependencies =
            getDependencies(constructorInjectionPoint, implementation);

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
          factoryRawType.cast(
              Proxy.newProxyInstance(
                  factoryRawType.getClassLoader(), new Class[] {factoryRawType}, this));

      Map<Method, AssistData> dataSoFactory = assistDataBuilder.build();
      ImmutableMap.Builder<Method, MethodHandle> methodHandleBuilder = ImmutableMap.builder();

      for (Map.Entry<String, Method> entry : defaultMethods.entries()) {
        Method defaultMethod = entry.getValue();
        MethodHandle handle =
            assistedFactoryMethodHandle.createMethodHandle(defaultMethod, this.factory);
        if (handle != null) {
          methodHandleBuilder.put(defaultMethod, handle);
        } else {
          boolean foundMatch = false;
          for (Method otherMethod : otherMethods.get(defaultMethod.getName())) {
            if (dataSoFactory.containsKey(otherMethod)
                && isCompatible(defaultMethod, otherMethod)) {
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

      this.assistData = assistDataBuilder.build();
      this.methodHandles = methodHandleBuilder.build();

      for (Map.Entry<Method, AssistData> entry : this.assistData.entrySet()) {
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
    } catch (ErrorsException exception) {
      throw new ConfigurationException(exception.getErrors().getMessages());
    }
  }

  private boolean isDefault(Method method) {
    return (method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC))
        == Modifier.PUBLIC;
  }

  private boolean isCompatible(Method source, Method destination) {
    if (!source.getReturnType().isAssignableFrom(destination.getReturnType())) {
      return false;
    }
    Class<?>[] sourceParameters = source.getParameterTypes();
    Class<?>[] destinationParameters = destination.getParameterTypes();
    return sourceParameters.length == destinationParameters.length
        && IntStream.range(0, sourceParameters.length)
            .allMatch(i -> sourceParameters[i].isAssignableFrom(destinationParameters[i]));
  }

  /** {@inheritDoc} */
  @Override
  public F get() {
    this.generate();
    return this.factory;
  }

  /** {@inheritDoc} */
  @Override
  public Set<Dependency<?>> getDependencies() {
    this.generate();

    Set<Dependency<?>> combinedDeps = new HashSet<>();
    for (AssistData data : assistData.values()) {
      combinedDeps.addAll(data.getDependencies());
    }
    return ImmutableSet.copyOf(combinedDeps);
  }

  /** {@inheritDoc} */
  @Override
  public Key<F> getKey() {
    return this.factoryKey;
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public Collection<AssistedMethod> getAssistedMethods() {
    this.generate();

    return (Collection<AssistedMethod>) (Collection<?>) this.assistData.values();
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public <T, V> V acceptExtensionVisitor(
      BindingTargetVisitor<T, V> visitor, ProviderInstanceBinding<? extends T> binding) {
    return visitor instanceof AssistedInjectTargetVisitor
        ? ((AssistedInjectTargetVisitor<T, V>) visitor).visit((AssistedInjectBinding<T>) this)
        : visitor.visit(binding);
  }

  private void validateFactoryReturnType(Errors errors, Class<?> returnType, Class<?> factoryType) {
    if (Modifier.isPublic(factoryType.getModifiers())
        && !Modifier.isPublic(returnType.getModifiers())) {
      errors.addMessage(
          "%s is public, but has a method that returns a non-public type: %s. "
              + "Due to limitations with java.lang.reflect.Proxy, this is not allowed. "
              + "Please either make the factory non-public or the return type public.",
          factoryType, returnType);
    }
  }

  /**
   * @return {@code true} if the {@link ConfigurationException} is due to an error of {@link
   *     TypeLiteral} not being fully specified.
   */
  private boolean isTypeNotSpecified(
      TypeLiteral<?> typeLiteral, ConfigurationException configurationException) {
    Collection<Message> messages = configurationException.getErrorMessages();
    return messages.size() == 1
        && getOnlyElement(new Errors().keyNotFullySpecified(typeLiteral).getMessages())
            .getMessage()
            .equals(getOnlyElement(messages).getMessage());
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
      for (InjectionPoint injectionPoint :
          InjectionPoint.forInstanceMethodsAndFields(implementation)) {
        builder.addAll(injectionPoint.getDependencies());
      }
    }
    return builder.build();
  }

  /**
   * Retrieves all non-assisted dependencies.
   *
   * @return All non-assisted dependencies.
   */
  private Set<Dependency<?>> removeAssistedDependencies(Set<Dependency<?>> dependencies) {
    ImmutableSet.Builder<Dependency<?>> builder = ImmutableSet.builder();
    for (Dependency<?> dependency : dependencies) {
      Class<?> annotationType = dependency.getKey().getAnnotationType();
      if (annotationType == null || !annotationType.equals(Assisted.class)) {
        builder.add(dependency);
      }
    }
    return builder.build();
  }

  /**
   * @return {@code true} if all dependencies are suitable for the optimized version of {@link
   *     AssistedInject}. The optimized version caches the binding & uses a {@link
   *     ThreadLocalProvider}, so can only be applied if the assisted bindings are immediately
   *     provided. This looks for hints that the values may be lazily retrieved, by looking for
   *     injections of Injector or a Provider for the assisted values.
   */
  private boolean isValidForOptimizedAssistedInject(Set<Dependency<?>> dependencies) {
    Set<Dependency<?>> badDependencies = null; // optimization: create lazily
    for (Dependency<?> dependency : dependencies) {
      if (isInjectorOrAssistedProvider(dependency)) {
        if (badDependencies == null) {
          badDependencies = Sets.newHashSet();
        }
        badDependencies.add(dependency);
      }
    }
    return badDependencies == null || badDependencies.isEmpty();
  }

  /**
   * Returns true if the dependency is for {@link Injector} or if the dependency is a {@link
   * Provider} for a parameter that is {@literal @}{@link Assisted}.
   */
  private boolean isInjectorOrAssistedProvider(Dependency<?> dependency) {
    Class<?> annotationType = dependency.getKey().getAnnotationType();

    return dependency
        .getKey()
        .getTypeLiteral()
        .getRawType()
        .equals(
            (annotationType != null && annotationType.equals(Assisted.class))
                ? Provider.class
                : Injector.class);
  }

  /**
   * @return A key similar to {@code key}, but with an {@link Assisted} binding annotation. This
   *     fails if another binding annotation is clobbered in the process. If the key already has the
   *     {@link Assisted} annotation, it is returned as-is to preserve any String value.
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
   * Creates a child injector that binds the {@code arguments}, and returns the binding for the
   * {@code method}'s result.
   *
   * @param method The method, which should be binding.
   * @param arguments The arguments, which should be binding.
   * @param data The assisted data.
   * @return The binding for the method's result.
   */
  public Binding<?> getBindingFromNewInjector(Method method, Object[] arguments, AssistData data) {
    final Key<?> returnType = data.getReturnType();
    final Key<?> returnKey = Key.get(returnType.getTypeLiteral(), RETURN_ANNOTATION);

    Injector childInjector =
        InjectionHolder.getInstance()
            .getInjector()
            .createChildInjector(new AssistedFactoryModule(method, arguments, data, returnKey));
    Binding<?> binding = childInjector.getBinding(returnKey);

    if (data.isOptimized()) {
      data.setCachedBinding(binding);
    }
    return binding;
  }

  /** {@inheritDoc} */
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

    AssistData data = this.assistData.get(method);
    checkState(data != null, "No data for method: %s", method);
    Provider<?> provider =
        data.getCachedBinding() != null
            ? data.getCachedBinding().getProvider()
            : getBindingFromNewInjector(method, args, data).getProvider();
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

  /**
   * @see #isDeclaredClassThrowable(Method, Throwable)
   * @see #isAnErrorOrRuntimeException(Throwable)
   */
  private boolean canRethrow(Method invokedMethod, Throwable throwable) {
    return isAnErrorOrRuntimeException(throwable)
        && isDeclaredClassThrowable(invokedMethod, throwable);
  }

  /**
   * Whether the exception types of the {@code invokedMethod} are instances of the given {@code
   * throwable}.
   *
   * @param invokedMethod The invoked method, to retrieves the exception types.
   * @param throwable The throwable to be checked.
   * @return {@code true} if the exception types of the invoked method are instances of the given
   *     throwable, otherwise {@code false}.
   */
  private boolean isDeclaredClassThrowable(Method invokedMethod, Throwable throwable) {
    for (Class<?> declaredType : invokedMethod.getExceptionTypes()) {
      if (declaredType.isInstance(throwable)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Whether the given {@code throwable} is an {@link Error} or {@link RuntimeException}.
   *
   * @param throwable The throwable to be checked.
   * @return {@code true} if the throwable is an {@link Error} or {@link RuntimeException},
   *     otherwise {@code false}.
   */
  private boolean isAnErrorOrRuntimeException(Throwable throwable) {
    return throwable instanceof Error || throwable instanceof RuntimeException;
  }
}
