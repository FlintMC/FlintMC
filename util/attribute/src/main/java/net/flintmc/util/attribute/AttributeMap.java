package net.flintmc.util.attribute;

import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface AttributeMap {

  /**
   * Registers a new {@link Attribute}.
   *
   * @param attribute The attribute to be registered.
   * @return This map for chaining.
   */
  AttributeMap register(Attribute attribute);

  /**
   * Retrieves an attribute with the given {@code key}.
   *
   * @param key The key of an attribute.
   * @return An attribute with the key or {@code null}.
   */
  Attribute get(String key);

  /**
   * Retrieves an array of all registered attributes.
   *
   * @return An array of all registered attributes.
   */
  Attribute[] values();

  /**
   * Retrieves an array of all registered attributes to be communicated to the client.
   *
   * @return An array of all registered attributes to be communicated to the client.
   */
  Attribute[] sharedValues();

  /**
   * A factory for the {@link AttributeMap}.
   */
  @AssistedFactory(AttributeMap.class)
  interface Factory {

    /**
     * Creates a new {@link AttributeMap}.
     *
     * @return A created attribute map.
     */
    AttributeMap create();
  }
}
