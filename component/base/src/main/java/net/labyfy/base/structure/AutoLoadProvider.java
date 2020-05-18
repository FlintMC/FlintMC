package net.labyfy.base.structure;

import com.google.common.collect.Multimap;

import java.util.Set;

public interface AutoLoadProvider {
  void registerAutoLoad(Multimap<Integer, Class<?>> autoLoadClasses);
}
