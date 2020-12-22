package net.flintmc.mcapi.items.inventory;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;

/**
 * Represents a type for the inventory.
 */
public interface InventoryType {

  /**
   * Retrieves the registry name of this inventory type.
   *
   * @return the registry name of this inventory type.
   */
  NameSpacedKey getRegistryName();

  /**
   * Retrieves the default title of this inventory type.
   *
   * @return the default title of this inventory type.
   */
  ChatComponent getDefaultTitle();

  /**
   * Retrieves the dimension of this inventory type.
   *
   * @return the dimension of this inventory type.
   */
  InventoryDimension getDefaultDimension();

  /**
   * Whether the dimension of this inventory type is customizable.
   *
   * @return {@code true} when the inventory type is customizable, otherwise {@code false}
   */
  boolean isCustomizableDimensions();

  /**
   * A builder class for {@link InventoryType}
   */
  interface Builder {

    /**
     * Sets the registry name for the inventory type.
     *
     * @param registryName The registry name for the built inventory
     * @return this builder, for chaining
     */
    Builder registryName(NameSpacedKey registryName);

    /**
     * Sets the default title for the inventory type.
     *
     * @param defaultTitle The default title for the built inventory.
     * @return this builder, for chaining
     */
    Builder defaultTitle(ChatComponent defaultTitle);

    /**
     * Sets the default dimension for the inventory type.
     *
     * @param dimension The dimension for the built inventory type.
     * @return this builder, for chaining
     */
    Builder defaultDimension(InventoryDimension dimension);

    /**
     * Sets the dimension of the inventory type to customizable.
     *
     * @return this builder, for chaining
     */
    Builder customizableDimensions();

    /**
     * Builds a new {@link InventoryType} with the parameters specified in this builder.
     *
     * <p>The only parameters that must be specified are {@link #registryName(NameSpacedKey)} and
     * {@link #defaultDimension(InventoryDimension)}.
     *
     * <p>If no {@link #defaultTitle(ChatComponent)} has been provided, a new {@link TextComponent}
     * will be used with the {@link #registryName(NameSpacedKey)}.
     *
     * @return the built inventory type.
     */
    InventoryType build();
  }

  /**
   * A factory class for {@link Builder}
   */
  @AssistedFactory(Builder.class)
  interface Factory {

    /**
     * Creates a new {@link Builder} to built an inventory type.
     *
     * @return the created builder.
     */
    Builder newBuilder();
  }
}
