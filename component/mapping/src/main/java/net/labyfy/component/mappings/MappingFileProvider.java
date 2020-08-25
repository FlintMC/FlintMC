package net.labyfy.component.mappings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@FunctionalInterface
public interface MappingFileProvider {
  /**
   * Get mapping input.
   *
   * @param version The Minecraft version.
   * @return Mapping inputs.
   * @throws IOException If the mappings could not be opened.
   */
  Map<String, InputStream> getMappings(String version) throws IOException;
}
