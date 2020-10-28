package net.labyfy.internal.component.config.generator.method.defaults;

import javassist.*;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.internal.component.config.generator.method.DefaultConfigMethod;

public abstract class FieldConfigMethod extends DefaultConfigMethod {
  public FieldConfigMethod(GeneratingConfig config, CtClass declaringClass, String configName, CtClass returnType) {
    super(config, declaringClass, configName, returnType);
  }

  protected CtField generateOrGetField(CtClass target) throws CannotCompileException {
    return this.generateOrGetField(target, null);
  }

  protected CtField generateOrGetField(CtClass target, String defaultValue) throws CannotCompileException {
    try {
      return target.getDeclaredField(this.configName);
    } catch (NotFoundException ignored) {
    }

    String fieldSrc = "private " + this.methodType.getName() + " " + this.configName;

    if (defaultValue == null && this.methodType.isInterface() && !super.isSerializableInterface()) {
      CtClass implementation = super.config.getGeneratedImplementation(this.methodType.getName());

      if (implementation != null) {
        fieldSrc += " = new " + implementation.getName() + "(this.config)";
      } else {
        fieldSrc += " = " + InjectionHolder.class.getName() + ".getInjectedInstance(" + this.methodType.getName() + ".class)";
      }
    }

    if (defaultValue != null) {
      fieldSrc += " = " + defaultValue;
    }

    CtField field = CtField.make(fieldSrc + ";", target);
    target.addField(field);
    return field;
  }

  protected boolean hasMethod(CtClass type, String name) {
    try {
      type.getDeclaredMethod(name);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  protected void insertSaveConfig(CtMethod method) throws CannotCompileException {
    CtClass declaring = method.getDeclaringClass();

    try {
      declaring.getField("config");
    } catch (NotFoundException e) {
      String configInterfaceName = super.getConfig().getBaseClass().getName();
      String configGetter = super.getConfig().getBaseClass().subclassOf(declaring)
          ? "this"
          : InjectionHolder.class.getName() + ".getInjectedInstance(" + configInterfaceName + ".class)";

      declaring.addField(CtField.make("private final transient " + configInterfaceName + " config = " + configGetter + ";", declaring));
    }

    String src = "this.configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);";
    if (method.getMethodInfo().getCodeAttribute() != null) {
      method.insertAfter(src);
    } else {
      method.setBody(src);
    }
  }

}
