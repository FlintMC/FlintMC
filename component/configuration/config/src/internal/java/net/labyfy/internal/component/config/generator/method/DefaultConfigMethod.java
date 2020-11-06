package net.labyfy.internal.component.config.generator.method;

import javassist.CtClass;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;
import net.labyfy.component.stereotype.service.CtResolver;

public abstract class DefaultConfigMethod implements ConfigMethod {

  protected final GeneratingConfig config;
  protected final CtClass declaringClass;
  protected final String configName;
  protected final CtClass methodType;

  protected boolean implementedMethods;
  protected boolean addedInterfaceMethods;
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
}
