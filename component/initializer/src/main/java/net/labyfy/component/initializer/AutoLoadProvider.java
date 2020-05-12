package net.labyfy.component.initializer;

import java.util.List;

public interface AutoLoadProvider {
  void registerAutoLoad(List<Class<?>> autoLoadClasses);
}
