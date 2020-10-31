package net.labyfy.internal.component.config.transform;

import net.labyfy.component.config.generator.GeneratingConfig;

public class TransformedConfigMeta {

  private final Class<?> superClass;
  private Class<?> implementationClass;
  private GeneratingConfig config;

  public TransformedConfigMeta(Class<?> superClass) {
    this.superClass = superClass;
  }

  public Class<?> getSuperClass() {
    return this.superClass;
  }

  public Class<?> getImplementationClass() {
    return this.implementationClass;
  }

  public void setImplementationClass(Class<?> implementationClass) {
    this.implementationClass = implementationClass;
  }

  public GeneratingConfig getConfig() {
    return this.config;
  }

  public void setConfig(GeneratingConfig config) {
    this.config = config;
  }
}
