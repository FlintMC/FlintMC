package net.labyfy.base.structure;

import com.google.common.collect.Multimap;
import net.labyfy.base.structure.util.TriConsumer;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public interface AutoLoadProvider {
  void registerAutoLoad(TriConsumer<Integer, Integer, String> integer);
}
