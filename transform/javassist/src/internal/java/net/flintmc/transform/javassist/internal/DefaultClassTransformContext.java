package net.flintmc.transform.javassist.internal;

import javassist.*;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DefaultClassTransformContext implements ClassTransformContext {

  private final ClassMappingProvider flintClassMappingProvider;
  private final CtClass ctClass;

  public DefaultClassTransformContext(
          ClassMappingProvider flintClassMappingProvider, CtClass ctClass) {
    this.flintClassMappingProvider = flintClassMappingProvider;
    this.ctClass = ctClass;
  }

  @Override
  public CtField getField(String name) throws NotFoundException {
    return this.ctClass.getField(this.flintClassMappingProvider.get(this.ctClass.getName()).getField(name).getName());
  }

  @Override
  public CtMethod addMethod(String returnType, String name, String body, Modifier... modifiers)
      throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtMethod ctMethod =
        CtMethod.make(
            String.format("%s %s %s {%s}", javaModifier, returnType, name, body), this.ctClass);
    this.ctClass.addMethod(ctMethod);
    return ctMethod;
  }

  @Override
  public CtMethod addMethod(String src) throws CannotCompileException {
    CtMethod make = CtMethod.make(src, this.ctClass);
    this.ctClass.addMethod(make);
    return make;
  }

  @Override
  public CtMethod getOwnerMethod(String name, String desc) throws NotFoundException {
    return this.ctClass.getMethod(name, desc);
  }

  @Override
  public CtField addField(Class<?> type, String name, Modifier... modifiers)
      throws CannotCompileException {
    return this.addField(type.getName(), name, modifiers);
  }

  @Override
  public CtField addField(String type, String name, Modifier... modifiers)
      throws CannotCompileException {
    return this.addField(type, name, "null", modifiers);
  }

  @Override
  public CtField addField(Class<?> type, String name, String value, Modifier... modifiers)
      throws CannotCompileException {
    return this.addField(type.getName(), name, value, modifiers);
  }

  @Override
  public CtField addField(String type, String name, String value, Modifier... modifiers)
      throws CannotCompileException {
    String javaModifier =
        Arrays.stream(modifiers).map(Modifier::toString).collect(Collectors.joining(" "));

    CtField make =
        CtField.make(
            String.format("%s %s %s = %s;", javaModifier, type, name, value), this.ctClass);
    this.ctClass.addField(make);
    return make;
  }

  @Override
  public CtMethod getDeclaredMethod(String name, Class<?>... classes) throws NotFoundException {
    CtClass[] ctClasses = new CtClass[classes.length];

    for (int i = 0; i < classes.length; i++) {
      ctClasses[i] = this.ctClass.getClassPool().get(classes[i].getName());
    }

    return this.getCtClass()
        .getDeclaredMethod(
            this.flintClassMappingProvider
                .get(this.ctClass.getName())
                .getMethod(name, classes)
                .getName(),
            ctClasses);
  }

  @Override
  public CtClass getCtClass() {
    return ctClass;
  }
}
