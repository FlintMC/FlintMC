package net.labyfy.component.mappings;

import java.io.InputStream;
import java.util.Map;

/**
 * Provides all mapping files needed.
 * Should not be implemented by a user.
 */
@FunctionalInterface
public interface MappingFileProvider {
  Map<String, InputStream> getMappings(String version);
}
