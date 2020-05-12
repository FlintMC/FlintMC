package net.labyfy.base.structure;

import java.util.Set;

public interface AutoLoadProvider {
  void registerAutoLoad(Set<Class<?>> autoLoadClasses);
}
