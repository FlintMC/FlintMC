package net.labyfy.internal.component.config.generator.method;

import javassist.*;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.serialization.ConfigSerializationService;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;
import net.labyfy.component.stereotype.service.CtResolver;

public abstract class DefaultConfigMethod implements ConfigMethod {

  protected final ConfigSerializationService serializationService;
  protected final GeneratingConfig config;
  protected final CtClass declaringClass;
  protected final String configName;
  protected final CtClass methodType;

  protected boolean implementedMethods;
  protected boolean addedInterfaceMethods;
  private String[] pathPrefix;

  public DefaultConfigMethod(ConfigSerializationService serializationService, GeneratingConfig config,
                             CtClass declaringClass, String configName, CtClass methodType) {
    this.serializationService = serializationService;
    this.config = config;
    this.declaringClass = declaringClass;
    this.configName = configName;
    this.methodType = methodType;
  }

  @Override
  public GeneratingConfig getConfig() {
    return this.config;
  }

  @Override
  public CtClass getDeclaringClass() {
    return this.declaringClass;
  }

  @Override
  public String getConfigName() {
    return this.configName;
  }

  @Override
  public CtClass getStoredType() {
    return this.methodType;
  }

  @Override
  public String[] getPathPrefix() {
    return this.pathPrefix;
  }

  @Override
  public void setPathPrefix(String[] pathPrefix) throws IllegalStateException {
    if (this.pathPrefix != null) {
      throw new IllegalStateException("The pathPrefix cannot be set twice");
    }
    this.pathPrefix = pathPrefix;
  }

  protected Class<?> loadImplementationOrDefault(CtClass superClass) {
    CtClass implementation = this.config.getGeneratedImplementation(superClass.getName());

    Class<?> primitiveType = PrimitiveTypeLoader.getWrappedClass(superClass.getName());
    if (primitiveType != null) { // primitive classes can't be loaded with the ClassLoader
      return primitiveType;
    }
    return CtResolver.get(implementation != null ? implementation : superClass);
  }

  @Override
  public void requireNoImplementation() {
    this.implementedMethods = true;
  }

  @Override
  public boolean hasImplementedExistingMethods() {
    return this.implementedMethods;
  }

  @Override
  public boolean hasAddedInterfaceMethods() {
    return this.addedInterfaceMethods;
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

    if (defaultValue == null && this.methodType.isInterface() && !this.serializationService.hasSerializer(this.getStoredType())) {
      CtClass implementation = this.config.getGeneratedImplementation(this.methodType.getName());

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
      String configName = this.config.getBaseClass().getName();

      String configGetter = "this";
      if (!this.config.getBaseClass().subclassOf(declaring)) {
        configGetter = InjectionHolder.class.getName() + ".getInjectedInstance(" + configName + ".class)";
      }

      declaring.addField(CtField.make("private final transient " + configName + " config = " + configGetter + ";", declaring));
    }

    String src = "this.configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);";
    if (method.getMethodInfo().getCodeAttribute() != null) {
      method.insertAfter(src);
    } else {
      method.setBody(src);
    }
  }

}
