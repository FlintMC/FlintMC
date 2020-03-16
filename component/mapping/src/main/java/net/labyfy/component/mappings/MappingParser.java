package net.labyfy.component.mappings;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

@FunctionalInterface
public interface MappingParser {

  Collection<ClassMapping> parse(Map<String, InputStream> input);
}
