package net.labyfy.internal.component.config.generator.method;

import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;

import java.util.Collection;
import java.util.Map;

public abstract class DefaultConfigMethod implements ConfigMethod {

  protected final GeneratingConfig config;
  protected final CtClass declaringClass;
  protected final String configName;
  protected final CtClass methodType;

  private String[] pathPrefix;

  public DefaultConfigMethod(GeneratingConfig config, CtClass declaringClass, String configName, CtClass methodType) {
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

  @Override
  public boolean isSerializableInterface() {
    try {
      return this.isSerializableInterface(this.methodType);
    } catch (NotFoundException e) {
      return false;
    }
  }

  @Override
  public boolean isSerializableInterface(CtClass type) throws NotFoundException {
    if (type.getName().equals(Map.class.getName())
        || type.getName().equals(Collection.class.getName())) {
      return true;
    }

    for (CtClass subType : type.getInterfaces()) {
      if (this.isSerializableInterface(subType)) {
        return true;
      }
    }

    return false;
  }

  protected Class<?> loadImplementationOrDefault(CtClass superClass) {
    ClassLoader classLoader = this.config.getClassLoader();
    CtClass implementation = this.config.getGeneratedImplementation(superClass.getName());
    String name = (implementation != null ? implementation : superClass).getName();

    try {
      return PrimitiveTypeLoader.loadClass(classLoader, name);
    } catch (ClassNotFoundException e) {
      String methodName = String.join(".", this.getPathPrefix()) + "." + this.getConfigName();
      throw new IllegalArgumentException("Failed to load class " + name + " for method " + methodName + " in config "
          + this.config.getName());
    }
  }

}
