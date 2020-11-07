package net.labyfy.internal.component.config.generator.method.defaults;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.serialization.ConfigSerializationService;
import net.labyfy.internal.component.config.generator.method.DefaultConfigMethod;

import java.lang.reflect.Type;

public class ConfigGetterSetter extends DefaultConfigMethod {

  public ConfigGetterSetter(ConfigSerializationService serializationService, GeneratingConfig config, CtClass declaringClass,
                            String name, CtClass methodType) {
    super(serializationService, config, declaringClass, name, methodType);
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
  public Type getSerializedType() {
    return super.loadImplementationOrDefault(super.getStoredType());
  }

  @Override
  public void generateMethods(CtClass target) throws CannotCompileException {
    if (!this.hasMethod(target, this.getGetterName())) {
      this.insertGetter(target);
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      this.insertSetter(target);
    }
  }

  @Override
  public void implementExistingMethods(CtClass target) throws CannotCompileException, NotFoundException {
    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write method to the setter
    try {
      this.insertSaveConfig(target.getDeclaredMethod(this.getSetterName()));
    } catch (NotFoundException e) {
      if (!super.getStoredType().isInterface() && !super.serializationService.hasSerializer(super.getStoredType())) {
        // ignore if it is an interface that will never get overridden by anything
        throw e;
      }
    }

    super.implementedMethods = true;
  }

  @Override
  public void addInterfaceMethods(CtClass target) throws CannotCompileException {
    if (!this.hasMethod(target, this.getGetterName())) {
      target.addMethod(CtNewMethod.make(this.methodType.getName() + " " + this.getGetterName() + "();", target));
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      target.addMethod(CtNewMethod.make("void " + this.getSetterName() + "(" + this.methodType.getName() + " arg);", target));
    }

    super.addedInterfaceMethods = true;
  }

  private void insertGetter(CtClass target) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), super.generateOrGetField(target)));
  }

  private void insertSetter(CtClass target) throws CannotCompileException {
    target.addMethod(CtNewMethod.make(
        "public void " + this.getSetterName() + "(" + this.methodType.getName() + " arg) { " +
            "this." + this.configName + " = arg;" +
            "this.configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);" +
            "}", target));
  }

}
