package net.flintmc.attribute;

/**
 * Represents an attribute.
 */
public interface Attribute {

  /**
   * Retrieves the name of the attribute.
   *
   * @return The attribute's name.
   */
  String getName();

  double clampValue(double value);

  /**
   * Retrieves the default value of the attribute.
   *
   * @return The attribute's default value.
   */
  double getDefaultValue();

  /**
   * Whether the attribute should be watched.
   *
   * @return {@code true} if the attribute should be watched, otherwise {@code false}.
   */
  boolean shouldWatch();

  /**
   * Changes whether the attribute should be watched.
   *
   * @param shouldWatch {@code true} if the attribute should be watched, otherwise {@code false}.
   * @return This attribute, for chaining.
   */
  Attribute setShouldWatch(boolean shouldWatch);

}
