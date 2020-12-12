package net.flintmc.framework.inject.internal.method;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.inject.method.MethodInjectorGenerationException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.DefaultValues;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.RootClassLoader;
import org.apache.logging.log4j.Logger;

/** {@inheritDoc} */
@Singleton
@Implement(MethodInjector.Factory.class)
public class MethodInjectorFactory implements MethodInjector.Factory {

  private final Logger logger;
  private final ClassPool pool;
  private final AtomicInteger idCounter;
  private final Map<Integer, MethodInjector> injectorCache;

  @Inject
  private MethodInjectorFactory(@InjectLogger Logger logger) {
    this.logger = logger;
    this.pool = ClassPool.getDefault();
    this.idCounter = new AtomicInteger();
    this.injectorCache = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public <T> T generate(CtMethod targetMethod, Class<T> ifc) {
    return this.generateAndCache(targetMethod, ifc);
  }

  /** {@inheritDoc} */
  @Override
  public <T> T generate(Object instance, CtMethod targetMethod, Class<T> ifc) {
    T t = this.generateAndCache(targetMethod, ifc);
    ((MethodInjector) t).setInstance(instance);
    return t;
  }

  @SuppressWarnings("unchecked")
  private <T> T generateAndCache(CtMethod targetMethod, Class<T> ifc) {
    int hash = CtResolver.hash(targetMethod);
    if (this.injectorCache.containsKey(hash)) {
      return (T) this.injectorCache.get(hash);
    }

    MethodInjector injector = this.generateInternal(targetMethod, ifc);

    this.injectorCache.put(hash, injector);
    return (T) injector;
  }

  private MethodInjector generateInternal(CtMethod targetMethod, Class<?> ifc) {
    if (!ifc.isInterface()) {
      throw new MethodInjectorGenerationException(
          "Cannot generate method injector for class "
              + ifc.getName()
              + " because it is not an interface");
    }

    try {
      return this.generateInternal0(targetMethod, ifc);
    } catch (Exception exception) {
      throw new MethodInjectorGenerationException(
          String.format(
              "Failed to generate a method injector for %s.%s based on the source from %s",
              targetMethod.getDeclaringClass().getName(), targetMethod.getName(), ifc.getName()),
          exception);
    }
  }

  private CtClass makeNewClass() {
    return this.pool.makeClass(
        "GeneratedMethodInjector_"
            + this.idCounter.incrementAndGet()
            + "_"
            + UUID.randomUUID().toString().replace("-", ""));
  }

  private MethodInjector generateInternal0(CtMethod targetMethod, Class<?> ifc)
      throws CannotCompileException, IOException, NotFoundException, ReflectiveOperationException {
    CtClass generated = this.makeNewClass();

    if (!targetMethod.getDeclaringClass().isFrozen()) {
      targetMethod.setModifiers(Modifier.setPublic(targetMethod.getModifiers()));
    }

    if (!Modifier.isPublic(targetMethod.getModifiers())) {
      this.logger.trace(
          "Cannot change accessor of method {}.{} to public, the method cannot be invoked",
          targetMethod.getDeclaringClass().getName(),
          targetMethod.getName());
    }

    this.addBase(generated, ifc, targetMethod.getDeclaringClass().getName());

    Method resolvedTargetMethod = CtResolver.get(targetMethod);
    List<Dependency<?>> targetDependencies = this.generateDependencies(resolvedTargetMethod);

    Object[] constantArgs = this.generateIndex(targetDependencies);

    for (Method method : ifc.getDeclaredMethods()) {
      CtMethod generatedMethod =
          this.generateImplementation(generated, targetDependencies, resolvedTargetMethod, method);
      generated.addMethod(generatedMethod);
    }

    return this.finalize(generated, constantArgs);
  }

  private void addBase(CtClass generated, Class<?> ifc, String targetClass)
      throws CannotCompileException, NotFoundException {
    // interfaces
    generated.addInterface(this.pool.get(MethodInjector.class.getName()));
    generated.addInterface(this.pool.get(ifc.getName()));

    // fields
    generated.addField(CtField.make(String.format("private %s instance;", targetClass), generated));
    generated.addField(CtField.make("private final Object[] constantArgs;", generated));

    // constructor
    generated.addConstructor(
        CtNewConstructor.make(
            String.format(
                "public %s(Object[] constantArgs) { this.constantArgs = constantArgs; }",
                generated.getName()),
            generated));

    // methods
    generated.addMethod(
        CtNewMethod.make(
            String.format(
                "private %s getCastedInstance() {"
                    + "if (this.instance == null) {"
                    + "  this.instance = (%1$s) %s.getInjectedInstance(%1$s.class);"
                    + "}"
                    + "return this.instance;"
                    + "}",
                targetClass, InjectionHolder.class.getName()),
            generated));
    generated.addMethod(
        CtNewMethod.make(
            "public Object getInstance() { return (Object) this.getCastedInstance(); }",
            generated));

    generated.addMethod(
        CtNewMethod.make(
            "public void setInstance(Object instance) {"
                + String.format("this.instance = (%s) instance;", targetClass)
                + "}",
            generated));
  }

  private CtMethod generateImplementation(
      CtClass declaring, List<Dependency<?>> targetDependencies, Method targetMethod, Method method)
      throws CannotCompileException {
    InjectionSource[] args = this.buildArgs(targetDependencies, targetMethod, method);

    StringBuilder body = new StringBuilder();
    boolean targetReturn = !targetMethod.getReturnType().equals(Void.TYPE);
    boolean sourceReturn = !method.getReturnType().equals(Void.TYPE);
    if (targetReturn && sourceReturn) {
      body.append("return ");
    }

    body.append("this.getCastedInstance().").append(targetMethod.getName()).append('(');

    for (int i = 0; i < args.length; i++) {
      InjectionSource source = args[i];
      body.append('(').append(this.buildTypeName(source.getType())).append(')');

      if (source.isFromInjector()) {
        body.append("this.constantArgs[").append(i).append(']');
      } else {
        body.append('$').append(source.getSourceIndex() + 1);
        // + 1 - Javassist uses 0 for this and the parameters begin at 1
      }

      if (i != args.length - 1) {
        body.append(',');
      }
    }

    body.append(");");

    if (sourceReturn && !targetReturn) {
      body.append("return ")
          .append(DefaultValues.getDefaultValue(method.getReturnType().getName()))
          .append(';');
    }

    return this.buildWrappedMethod(method, body.toString(), declaring);
  }

  private InjectionSource[] buildArgs(
      List<Dependency<?>> targetDependencies, Method targetMethod, Method method) {
    InjectionSource[] args = new InjectionSource[targetDependencies.size()];

    List<Dependency<?>> dependencies = this.generateDependencies(method);
    Class<?>[] targetParameters = targetMethod.getParameterTypes();

    for (int targetIndex = 0; targetIndex < targetDependencies.size(); targetIndex++) {
      Key<?> key = targetDependencies.get(targetIndex).getKey();
      int sourceIndex = this.getIndex(dependencies, key);
      Class<?> type = targetParameters[targetIndex];

      args[targetIndex] = new InjectionSource(sourceIndex, type);
    }

    return args;
  }

  private int getIndex(List<Dependency<?>> dependencies, Key<?> key) {
    for (int i = 0; i < dependencies.size(); i++) {
      if (dependencies.get(i).getKey().equals(key)) {
        return i;
      }
    }
    return -1;
  }

  private CtMethod buildWrappedMethod(Method wrapped, String body, CtClass declaring)
      throws CannotCompileException {
    return CtMethod.make(
        String.format(
            "public %s %s(%s) {" + body + "}",
            this.buildTypeName(wrapped.getReturnType()),
            wrapped.getName(),
            this.buildSourceParameterList(wrapped)),
        declaring);
  }

  private String buildSourceParameterList(Method method) {
    StringBuilder parameters = new StringBuilder();
    int i = 0;
    for (Class<?> parameter : method.getParameterTypes()) {
      parameters.append(this.buildTypeName(parameter)).append(" arg").append(i++).append(", ");
    }

    return parameters.length() == 0 ? "" : parameters.substring(0, parameters.length() - 2);
  }

  private String buildTypeName(Class<?> type) {
    String suffix = "";

    if (type.isArray()) {
      StringBuilder array = new StringBuilder();
      while (type.isArray()) {
        array.append("[]");
        type = type.getComponentType();
      }
      suffix = array.toString();
    }

    return type.getName() + suffix;
  }

  private List<Dependency<?>> generateDependencies(Method method) {
    return InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
        .getDependencies();
  }

  private Object[] generateIndex(List<Dependency<?>> dependencies) {
    Object[] index = new Object[dependencies.size()];
    Injector injector = InjectionHolder.getInstance().getInjector();

    for (int i = 0; i < index.length; i++) {
      Dependency<?> dependency = dependencies.get(i);

      try {
        index[i] = injector.getInstance(dependency.getKey());
      } catch (ConfigurationException ignored) {
        // the instance is not bound and should probably be taken from the parameters of the method
      }
    }

    return index;
  }

  private MethodInjector finalize(CtClass generated, Object[] args)
      throws IOException, CannotCompileException, ReflectiveOperationException {
    RootClassLoader loader = LaunchController.getInstance().getRootLoader();

    byte[] bytes = generated.toBytecode();
    Class<?> resolved = loader.commonDefineClass(generated.getName(), bytes, 0, bytes.length, null);

    return (MethodInjector)
        resolved.getDeclaredConstructor(Object[].class).newInstance(new Object[] {args});
  }
}
