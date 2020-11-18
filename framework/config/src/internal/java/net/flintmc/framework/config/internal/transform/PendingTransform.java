package net.flintmc.framework.config.internal.transform;

import net.flintmc.framework.config.generator.method.ConfigMethod;

public class PendingTransform {

  private final ConfigMethod method;

  public PendingTransform(ConfigMethod method) {
    this.method = method;
  }

  public ConfigMethod getMethod() {
    return this.method;
  }
}
