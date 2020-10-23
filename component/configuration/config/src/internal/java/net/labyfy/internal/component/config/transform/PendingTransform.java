package net.labyfy.internal.component.config.transform;

import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;

public class PendingTransform {

  private final GeneratingConfig config;
  private final ConfigMethod method;

  public PendingTransform(GeneratingConfig config, ConfigMethod method) {
    this.config = config;
    this.method = method;
  }

  public GeneratingConfig getConfig() {
    return this.config;
  }

  public ConfigMethod getMethod() {
    return this.method;
  }
}
