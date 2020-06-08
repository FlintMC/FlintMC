package net.labyfy.component.mappings;

import java.io.InputStream;
import java.util.Map;

@FunctionalInterface
public interface MappingFileProvider {
  Map<String, InputStream> getMappings(String version);
}
