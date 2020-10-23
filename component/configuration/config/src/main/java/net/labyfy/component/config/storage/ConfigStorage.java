package net.labyfy.component.config.storage;

import net.labyfy.component.config.generator.ParsedConfig;

public interface ConfigStorage {

  String getName();

  void write(ParsedConfig config);

  void read(ParsedConfig config);

}
