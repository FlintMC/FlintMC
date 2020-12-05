package net.flintmc.framework.config.internal.generator.method.defaults;

import com.google.common.base.Defaults;
import com.google.gson.reflect.TypeToken;
import javassist.*;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.internal.generator.method.DefaultConfigMethod;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigMultiGetterSetter extends DefaultConfigMethod {

  public static final String ALL_PREFIX = "All";

  private final CtClass keyType;
  private final CtClass valueType;

  public ConfigMultiGetterSetter(
      ConfigSerializationService serializationService,
      GeneratingConfig config,
      CtClass declaringClass,
      String name,
      CtClass methodType,
      CtClass keyType,
      CtClass valueType) {
    super(serializationService, config, declaringClass, name, methodType);
    this.keyType = keyType;
    this.valueType = valueType;
  }

  @Override
  public String getGetterName() {
    return "get" + ALL_PREFIX + super.getConfigName();
  }

  private String getSingleGetterName() {
    String prefix =
        this.valueType.equals(CtClass.booleanType) || this.valueType.getName().equals(Boolean.class.getName())
            ? "is"
            : "get";
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
    return new String[] {
      this.getGetterName(),
      this.getSingleGetterName(),
      this.getSetterName(),
      this.getSingleSetterName()
    };
  }

  @Override
  public CtClass[] getTypes() {
    return new CtClass[] {this.keyType, this.getStoredType(), this.valueType};
  }

  @Override
  public Type getSerializedType() {
    return TypeToken.getParameterized(
            Map.class,
            super.loadImplementationOrDefault(this.keyType),
            super.loadImplementationOrDefault(this.valueType))
        .getType();
  }

  @Override
  public void generateMethods(CtClass target) throws CannotCompileException {
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
  public void implementExistingMethods(CtClass target)
      throws CannotCompileException, NotFoundException {
    boolean generatedSetter = false;

    // generate the getter/setter for all values automatically if the key is an enum
    if (this.keyType.isEnum()) {
      if (!this.hasMethod(target, this.getGetterName())) {
        this.insertEnumGetAll(target);
      }

      if (!this.hasMethod(target, this.getSetterName())) {
        this.insertEnumSetAll(target);
        generatedSetter = true;
      }
    }

    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write methods to the setters
    if (!generatedSetter) {
      // only if the setter hasn't been generated, then the single setters will
      // be called and the saveConfig method called from there
      this.insertSaveConfig(target.getDeclaredMethod(this.getSetterName()));
    }
    this.insertSaveConfig(target.getDeclaredMethod(this.getSingleSetterName()));

    super.implementedMethods = true;
  }

  @Override
  public void addInterfaceMethods(CtClass target) throws CannotCompileException {
    if (this.keyType.isEnum()) {

      if (!this.hasMethod(target, this.getGetterName())) {
        target.addMethod(CtNewMethod.make("java.util.Map " + this.getGetterName() + "();", target));
      }

      if (!this.hasMethod(target, this.getSetterName())) {
        target.addMethod(
            CtNewMethod.make("void " + this.getSetterName() + "(java.util.Map arg);", target));
      }
    }

    super.addedInterfaceMethods = true;
  }

  private void insertEnumGetAll(CtClass target) throws CannotCompileException {
    String valueFormat = "%s";
    if (this.valueType.isPrimitive()) {
      valueFormat = ((CtPrimitiveType) this.valueType).getWrapperName() + ".valueOf(%s)";
    }

    StringBuilder operations = new StringBuilder();
    for (CtField field : this.keyType.getFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        String constant = this.keyType.getName() + "." + field.getName();
        operations
            .append("map.put(")
            .append(constant)
            .append(", ")
            .append(
                String.format(
                    valueFormat, "this." + this.getSingleGetterName() + "(" + constant + ")"))
            .append(");");
      }
    }

    target.addMethod(
        CtNewMethod.make(
            "public java.util.Map "
                + this.getGetterName()
                + "() {"
                + "java.util.Map map = new java.util.HashMap();"
                + operations
                + "return map;"
                + "}",
            target));
  }

  private void insertEnumSetAll(CtClass target) throws CannotCompileException {
    StringBuilder operations = new StringBuilder();
    for (CtField field : this.keyType.getFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        String constant = this.keyType.getName() + "." + field.getName();

        String valueName =
            this.valueType instanceof CtPrimitiveType
                ? ((CtPrimitiveType) this.valueType).getWrapperName()
                : this.valueType.getName();
        String getter = "((" + valueName + ") map.get(" + constant + "))";

        operations
            .append("if (map.containsKey(")
            .append(constant)
            .append(")) {")
            .append("this.")
            .append(this.getSingleSetterName())
            .append("(")
            .append(constant)
            .append(", ")
            .append(PrimitiveTypeLoader.asPrimitiveSource(this.valueType, getter))
            .append(");")
            .append("}");
      }
    }

    target.addMethod(
        CtNewMethod.make(
            "public void " + this.getSetterName() + "(java.util.Map map) {" + operations + "}",
            target));
  }

  private void insertGetAll(CtClass target, CtField field) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), field));
  }

  private void insertSetAll(CtClass target, CtField field) throws CannotCompileException {
    target.addMethod(
        CtNewMethod.make(
            "public void "
                + this.getSetterName()
                + "(java.util.Map map) { this."
                + field.getName()
                + " = map; }",
            target));
  }

  private void insertSetter(CtClass target, CtField field) throws CannotCompileException {
    String value = PrimitiveTypeLoader.asWrappedPrimitiveSource(this.valueType, "value");

    target.addMethod(
        CtNewMethod.make(
            "public void "
                + this.getSingleSetterName()
                + "("
                + this.keyType.getName()
                + " key, "
                + this.valueType.getName()
                + " value) {"
                + "this."
                + field.getName()
                + ".put(key, "
                + value
                + ");"
                + "this.configStorageProvider.write(("
                + ParsedConfig.class.getName()
                + ") this.config);"
                + "}",
            target));
  }

  private void insertGetter(CtClass target, CtField field) throws CannotCompileException {
    String defaultValue;
    try {
      defaultValue =
          this.valueType.isPrimitive()
              ? String.valueOf(
                  Defaults.defaultValue(
                      PrimitiveTypeLoader.loadClass(
                          super.getClass().getClassLoader(), this.valueType.getName())))
              : "null";
    } catch (ClassNotFoundException e) {
      defaultValue = "null";
    }

    String getter =
        PrimitiveTypeLoader.asWrappedPrimitiveSource(
            this.valueType,
            "this." + field.getName() + ".getOrDefault(input, " + defaultValue + ")");

    target.addMethod(
        CtNewMethod.make(
            "public "
                + this.valueType.getName()
                + " "
                + this.getSingleGetterName()
                + "("
                + this.keyType.getName()
                + " input) {"
                + "return ("
                + this.valueType.getName()
                + ") "
                + getter
                + ";"
                + "}",
            target));
  }
}
