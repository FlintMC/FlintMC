package net.labyfy.component.config.generator;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.util.Collection;

/**
 * Represents the implementation of an interface that has been marked with {@link Config} and been marked as a config by
 * the {@link ConfigGenerator}. This interface is not intended to be implemented manually.
 */
public interface ParsedConfig {

  /**
   * Retrieves the name of this config. The name is just the name of the interface (with its package) to be unique.
   *
   * @return The non-null name of this config
   */
  String getConfigName();

  /**
   * Retrieves a collection of all entries in this config. This may be modified to manually change the entries, but it
   * is not recommended.
   *
   * @return The non-null collection
   */
  Collection<ConfigObjectReference> getConfigReferences();

}
