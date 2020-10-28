package net.labyfy.internal.component.config.generator.method.defaults;

import com.google.common.base.Defaults;
import com.google.gson.reflect.TypeToken;
import javassist.*;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigMultiGetterSetter extends FieldConfigMethod {

  public static final String ALL_PREFIX = "All";

  private final CtClass keyType;
  private final CtClass valueType;

  public ConfigMultiGetterSetter(GeneratingConfig config, CtClass declaringClass, String name, CtClass methodType, CtClass keyType, CtClass valueType) {
    super(config, declaringClass, name, methodType);
    this.keyType = keyType;
    this.valueType = valueType;
  }

  @Override
  public String getGetterName() {
    return "get" + ALL_PREFIX + super.getConfigName();
  }

  private String getSingleGetterName() {
    CtClass type = super.getStoredType();

    String prefix = type.equals(CtClass.booleanType) || type.getName().equals(Boolean.class.getName()) ? "is" : "get";
    return prefix + super.getConfigName();
  }

  @Override
  public String getSetterName() {
    return "set" + ALL_PREFIX + super.getConfigName();
  }

  private String getSingleSetterName() {
    return "set" + super.getConfigName();
  }

  @Override
  public String[] getMethodNames() {
    return new String[]{this.getGetterName(), this.getSingleGetterName(), this.getSetterName()};
  }

  @Override
  public CtClass[] getTypes() {
    return new CtClass[]{this.keyType, this.getStoredType(), this.valueType};
  }

  @Override
  public Type getSerializedType() {
    return TypeToken.getParameterized(
        Map.class,
        super.loadImplementationOrDefault(this.keyType),
        super.loadImplementationOrDefault(this.valueType)
    ).getType();
  }

  @Override
  public void generateMethods(CtClass target)
      throws CannotCompileException {
    CtField field = super.generateOrGetField(target, "new java.util.HashMap()");

    if (!this.hasMethod(target, this.getGetterName())) {
      this.insertGetAll(target, field);
    }

    if (!this.hasMethod(target, this.getSingleGetterName())) {
      this.insertGetter(target, field);
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      this.insertSetAll(target, field);
    }

    if (!this.hasMethod(target, this.getSingleSetterName())) {
      this.insertSetter(target, field);
    }
  }

  @Override
  public void implementExistingMethods(CtClass target) throws CannotCompileException, NotFoundException {
    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write methods to the setters
    this.insertSaveConfig(target.getDeclaredMethod(this.getSetterName()));
  }

  private void insertGetAll(CtClass target, CtField field) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), field));
  }

  private void insertSetAll(CtClass target, CtField field) throws CannotCompileException {
    target.addMethod(CtNewMethod.make(
        "public void " + this.getSetterName() + "(java.util.Map map) { this." + field.getName() + " = map; }", target
    ));
  }

  private void insertSetter(CtClass target, CtField field) throws CannotCompileException {
    String value = this.replacePrimitive(this.valueType, "value");

    target.addMethod(CtNewMethod.make(
        "public void " + this.getSingleSetterName() + "(" + this.keyType.getName() + " key, " + this.valueType.getName() + " value) {" +
            "this." + field.getName() + ".put(key, " + value + ");" +
            "this.configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);" +
            "}", target
    ));
  }

  private void insertGetter(CtClass target, CtField field) throws CannotCompileException {
    String defaultValue;
    try {
      defaultValue = this.valueType.isPrimitive() ? String.valueOf(Defaults.defaultValue(Class.forName(this.valueType.getName()))) : "null";
    } catch (ClassNotFoundException e) {
      defaultValue = "null";
    }

    String getter = this.replacePrimitive(this.valueType, "this." + field.getName() + ".getOrDefault(input, " + defaultValue + ")");

    target.addMethod(CtNewMethod.make(
        "public " + this.valueType.getName() + " " + this.getSingleGetterName() + "(" + this.keyType.getName() + " input) {" +
            "return (" + this.valueType.getName() + ") " + getter + ";" +
            "}", target
    ));
  }

  private String replacePrimitive(CtClass type, String primitive) {
    if (type.isPrimitive()) {
      return ((CtPrimitiveType) type).getWrapperName() + ".valueOf((" + type.getName() + ") " + primitive + ")";
    }
    return primitive;
  }

}
