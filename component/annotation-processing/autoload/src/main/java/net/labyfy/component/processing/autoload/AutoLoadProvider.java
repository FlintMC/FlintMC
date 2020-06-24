package net.labyfy.component.processing.autoload;

import net.labyfy.component.commons.consumer.TriConsumer;

public interface AutoLoadProvider {
  void registerAutoLoad(TriConsumer<Integer, Integer, String> integer);
}
