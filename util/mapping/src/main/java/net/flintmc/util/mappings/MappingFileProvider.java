package net.flintmc.util.mappings;

import com.google.inject.ImplementedBy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@FunctionalInterface
@ImplementedBy(DefaultMappingFileProvider.class)
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
