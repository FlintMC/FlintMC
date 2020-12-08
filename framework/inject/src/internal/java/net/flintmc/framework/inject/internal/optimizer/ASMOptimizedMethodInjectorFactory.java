package net.flintmc.framework.inject.internal.optimizer;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.OptimizedMethodInjector;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.RootClassLoader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Implement(OptimizedMethodInjector.ASMFactory.class)
public class ASMOptimizedMethodInjectorFactory implements OptimizedMethodInjector.ASMFactory {

  private final FallbackOptimizedMethodInjector.BackupFactory backupFactory;
  private final AtomicInteger idCounter;

  @Inject
  private ASMOptimizedMethodInjectorFactory(
      FallbackOptimizedMethodInjector.BackupFactory backupFactory) {
    this.backupFactory = backupFactory;
    this.idCounter = new AtomicInteger();
  }

  private InternalOptimizedMethodInjector finalize(CtClass generated)
      throws IOException, CannotCompileException {
    RootClassLoader loader = LaunchController.getInstance().getRootLoader();

    byte[] bytes = generated.toBytecode();
    Class<?> resolved = loader.commonDefineClass(generated.getName(), bytes, 0, bytes.length, null);

    return (InternalOptimizedMethodInjector) InjectionHolder.getInjectedInstance(resolved);
  }

  @Override
  public OptimizedMethodInjector generate(String targetClass, String methodName) {
    return this.generateInternal(targetClass, methodName);
  }

  @Override
  public OptimizedMethodInjector generate(Object instance, String targetClass, String methodName) {
    InternalOptimizedMethodInjector injector = this.generateInternal(targetClass, methodName);
    injector.setInstance(instance);
    return injector;
  }

  private InternalOptimizedMethodInjector generateInternal(String targetClass, String methodName) {
    try {
      return this.generateInternal0(targetClass, methodName);
    } catch (CannotCompileException | IOException | NotFoundException exception) {
      exception.printStackTrace(); // TODO replace with logger

      try {
        return this.backupFactory.create(
            CtResolver.get(ClassPool.getDefault().getMethod(targetClass, methodName)));
      } catch (NotFoundException notFound) {
        throw new IllegalArgumentException(
            String.format("Method %s.%s not found", targetClass, methodName), notFound);
      }
    }
  }

  private InternalOptimizedMethodInjector generateInternal0(String targetClass, String methodName)
      throws CannotCompileException, IOException, NotFoundException {
    CtMethod method = ClassPool.getDefault().getMethod(targetClass, methodName);
    CtClass generated =
        ClassPool.getDefault()
            .makeClass(
                "OptimizedMethodInjector_"
                    + this.idCounter.incrementAndGet()
                    + "_"
                    + UUID.randomUUID().toString().replace("-", ""));

    generated.addInterface(ClassPool.getDefault().get(OptimizedMethodInjector.class.getName()));
    generated.addInterface(
        ClassPool.getDefault().get(InternalOptimizedMethodInjector.class.getName()));

    generated.addField(CtField.make("private Object[] index;", generated));
    generated.addField(CtField.make("private java.util.List dependencies;", generated));
    CtField instanceField =
        CtField.make(String.format("private %s instance;", targetClass), generated);
    generated.addField(instanceField);
    generated.addField(
        CtField.make(
            "private boolean fullyOptimized;",
            generated)); // only bindings that are available in the injector

    generated.addMethod(
        CtNewMethod.make(
            String.format(
                "public void init(java.lang.reflect.Method method) {"
                    + "this.dependencies = %s.generateDependencies(method);"
                    + "this.index = %1$s.generateIndex(this.dependencies);"
                    + "this.fullyOptimized = %1$s.isFullyOptimized(this.index);"
                    + "}",
                super.getClass().getName()),
            generated));

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

    String src =
        "public Object invoke(java.util.Map availableArguments) {"
            + "Object[] args = this.index;"
            + "if (!this.fullyOptimized) {"
            + String.format(
                "args = %s.generateArgs(this.index, this.dependencies, availableArguments);",
                super.getClass().getName())
            + "}";

    StringBuilder argsBuilder = new StringBuilder();
    CtClass[] parameters = method.getParameterTypes();
    for (int i = 0; i < parameters.length; i++) {
      argsBuilder
          .append("(")
          .append(parameters[i].getName())
          .append(") args[")
          .append(i)
          .append("], ");
    }

    String args =
        argsBuilder.length() == 0 ? "" : argsBuilder.substring(0, argsBuilder.length() - 2);
    String invoke = "this.getCastedInstance()." + methodName + "(" + args + ");";

    if (method.getReturnType().equals(CtClass.voidType)) {
      src += invoke + " return (Object) null;";
    } else {
      src += "return " + invoke;
    }

    src += "}";

    generated.addMethod(CtNewMethod.make(src, generated));

    InternalOptimizedMethodInjector injector = this.finalize(generated);
    injector.init(CtResolver.get(method));
    return injector;
  }

  /** Called from generated code, see above. */
  public static Object[] generateArgs(
      Object[] index, List<Dependency<?>> dependencies, Map<Key<?>, ?> availableArguments) {
    Object[] args = new Object[index.length];

    for (int i = 0; i < index.length; i++) {
      if (index[i] != null) {
        continue;
      }

      args[i] = availableArguments.get(dependencies.get(i).getKey());
    }

    return args;
  }

  /** Called from generated code, see above. */
  public static List<Dependency<?>> generateDependencies(Method method) {
    return InjectionPoint.forMethod(method, TypeLiteral.get(method.getDeclaringClass()))
        .getDependencies();
  }

  /** Called from generated code, see above. */
  public static Object[] generateIndex(List<Dependency<?>> dependencies) {
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

  /** Called from generated code, see above. */
  public static boolean isFullyOptimized(Object[] index) {
    for (Object o : index) {
      if (o == null) {
        return false;
      }
    }

    return true;
  }
}
