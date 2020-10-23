package net.labyfy.internal.component.config.generator.method.defaults;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;

import java.lang.reflect.Type;

public class ConfigGetterSetter extends FieldConfigMethod {

  public ConfigGetterSetter(CtClass declaringClass, String name, CtClass methodType) {
    super(declaringClass, name, methodType);
  }

  @Override
  public String getGetterName() {
    CtClass type = super.getStoredType();

    String prefix = type.equals(CtClass.booleanType) || type.getName().equals(Boolean.class.getName()) ? "is" : "get";
    return prefix + super.getConfigName();
  }

  @Override
  public String getSetterName() {
    return "set" + super.getConfigName();
  }

  @Override
  public String[] getMethodNames() {
    return new String[]{this.getGetterName(), this.getSetterName()};
  }

  @Override
  public CtClass[] getTypes() {
    return new CtClass[]{super.getStoredType()};
  }

  @Override
  public Type getSerializedType(GeneratingConfig config) {
    return super.loadImplementationOrDefault(super.getStoredType(), config);
  }

  @Override
  public void generateMethods(CtClass target, GeneratingConfig config) throws CannotCompileException {
    if (!this.hasMethod(target, this.getGetterName())) {
      this.insertGetter(target, config);
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      this.insertSetter(target);
    }
  }

  @Override
  public void implementExistingMethods(CtClass target, GeneratingConfig config) throws CannotCompileException, NotFoundException {
    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write method to the setter
    this.insertSaveConfig(target.getDeclaredMethod(this.getSetterName()));
  }

  private void insertGetter(CtClass target, GeneratingConfig config) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), super.generateOrGetField(target, config)));
  }

  private void insertSetter(CtClass target) throws CannotCompileException {
    target.addMethod(CtNewMethod.make(
        "public void " + this.getSetterName() + "(" + this.methodType.getName() + " arg) { " +
            "this." + this.configName + " = arg;" +
            "this.configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);" +
            "}", target));
  }

}
