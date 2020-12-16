package net.flintmc.util.attribute;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents an attribute for entities. */
public interface Attribute {

  /**
   * Retrieves the unique identifier of this attribute.
   *
   * @return The attribute's unique identifier.
   */
  String getKey();

  /**
   * Retrieves the attribute default value that should be applied.
   *
   * @return The default attribute value.
   */
  float getDefaultValue();

  /**
   * Retrieves the maximum value applicable to an entity for this attribute.
   *
   * @return The maximum value of this attribute.
   */
  float getMaximumValue();

  /**
   * Whether the attribute is shared to the client.
   *
   * @return {@code true} if the attribtue is shared to the client, otherwise {@code false}.
   */
  boolean isShared();

  @AssistedFactory(Attribute.class)
  interface Factory {

    /**
     * Creates a new {@link Attribute} wit the {@code key}, {@code defaultValue} and the {@code
     * maximumValue}.
     *
     * @param key The registry key for this attribute.
     * @param defaultValue The default value for this attribute.
     * @param maximumValue The maximum value for this attribute.
     * @return A created attribute.
     */
    Attribute create(
        @Assisted("key") String key,
        @Assisted("defaultValue") float defaultValue,
        @Assisted("maximumVale") float maximumValue);

    /**
     * Creates a new {@link Attribute} with the {@code key}, {@code defaultValue}, {@code
     * maximumValue} and {@code shared} if this attribute is to be used by the client.
     *
     * @param key The registry key for this attribute.
     * @param defaultValue The default value for this attribute.
     * @param maximumValue The maximum value for this attribute.
     * @param shared {@code true} if this attribute is to be used by the client, otherwise {@code
     *     false}.
     * @return A created attribute.
     */
    Attribute create(
        @Assisted("key") String key,
        @Assisted("defaultValue") float defaultValue,
        @Assisted("maximumValue") float maximumValue,
        @Assisted("shared") boolean shared);
  }
}
