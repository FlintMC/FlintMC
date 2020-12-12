package net.flintmc.framework.inject.internal.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.method.MethodInjectionUtils;

@Singleton
@Implement(MethodInjectionUtils.class)
public class DefaultMethodInjectionUtils implements MethodInjectionUtils {

  private static final String INJECTOR_NAME =
      "net.flintmc.framework.inject.method.OptimizedMethodInjector";
  private static final String FACTORY_NAME =
      "net.flintmc.framework.inject.method.OptimizedMethodInjector$Factory";

  private final InjectionUtils injectionUtils;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private DefaultMethodInjectionUtils(
      InjectionUtils injectionUtils, InjectedFieldBuilder.Factory fieldBuilderFactory) {
    this.injectionUtils = injectionUtils;
    this.fieldBuilderFactory = fieldBuilderFactory;
  }

  @Override
  public CtMethod generateOptimizedInjector(CtClass target, CtMethod targetMethod)
      throws CannotCompileException {
    return this.generateOptimizedInjector(
        target, targetMethod.getDeclaringClass().getName(), targetMethod.getName());
  }

  @Override
  public CtMethod generateOptimizedInjector(CtClass target, String targetClass, String methodName)
      throws CannotCompileException {
    CtField injectedFactory =
        this.fieldBuilderFactory.create().target(target).inject(FACTORY_NAME).generate();

    String injectorName = this.injectionUtils.generateInjectedFieldName() + "_" + methodName;

    CtField injectorField =
        CtField.make(String.format("private static %s %s;", INJECTOR_NAME, injectorName), target);
    target.addField(injectorField);

    String getterName = "getOptimizedMethodInjector_" + injectorName;
    String generate =
        String.format(
            "if (%s == null) { %1$s = %s.generate(\"%s\", \"%s\"); }",
            injectorName, injectedFactory.getName(), targetClass, methodName);
    CtMethod getter =
        CtMethod.make(
            String.format(
                "public static %s %s() { %s return %s; }",
                INJECTOR_NAME, getterName, generate, injectorName),
            target);
    target.addMethod(getter);

    return getter;
  }
}
