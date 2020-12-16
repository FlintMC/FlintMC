package net.flintmc.util.mappings;

import java.io.InputStream;
import java.util.Map;
import net.flintmc.util.mappings.exceptions.MappingParseException;

public interface MappingParser {

  /**
   * Parse mappings.
   *
   * @param input Mapping input.
   * @return A look-up table mapping obfuscated class names to class mappings.
   * @throws MappingParseException If the mapping cannot be parsed.
   */
  Map<String, ClassMapping> parse(final Map<String, InputStream> input)
      throws MappingParseException;
}
