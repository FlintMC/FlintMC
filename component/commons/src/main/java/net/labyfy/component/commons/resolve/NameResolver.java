package net.labyfy.component.commons.resolve;

/**
 * Generic type for resolving/mapping strings.
 */
@FunctionalInterface
public interface NameResolver extends Resolver<String, String> {
  /**
   * Resolves/maps one string to another.
   *
   * @param name The string to map
   * @return The mapped string
   */
  String resolve(String name);
}
