package net.labyfy.component.config.event;

import net.labyfy.component.config.generator.ParsedConfig;

// TODO implement
public interface ConfigEvent {

  Type getType();

  ParsedConfig getConfig();

  enum Type {

    LOAD,
    SAVE

  }

}
