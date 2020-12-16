package net.flintmc.mcapi.items.type;

import java.util.function.Supplier;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemRegistry;

/**
 * A category containing multiple items in the creative menu.
 */
public class ItemCategory {

  private final NameSpacedKey registryName;
  private final ChatComponent displayName;
  private final Supplier<ItemType> iconSupplier;
  private final boolean showTitle;
  private final int index;

  private ItemCategory(
      NameSpacedKey registryName,
      ChatComponent displayName,
      Supplier<ItemType> iconSupplier,
      boolean showTitle,
      int index) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.index = index;
    this.showTitle = showTitle;
    this.iconSupplier = iconSupplier;
  }

  /**
   * Creates a new category without any items to be registered in an {@link ItemRegistry}.
   *
   * @param registryName The non-null key in the registry, unique per {@link ItemRegistry}
   * @param displayName  The non-null display name of the category in the client
   * @param iconSupplier The non-null supplier to retrieve the icon to be displayed in the creative
   *                     menu in the client, the type retrieved by the supplier also needs to be
   *                     non-null
   * @param showTitle    Whether the title should be drawn by the client or not
   * @param index        The index of the category for sorting in the creative menu
   * @return The new non-null category for items
   * @see ItemRegistry#registerCategory(ItemCategory)
   */
  public static ItemCategory create(
      NameSpacedKey registryName,
      ChatComponent displayName,
      Supplier<ItemType> iconSupplier,
      boolean showTitle,
      int index) {
    return new ItemCategory(registryName, displayName, iconSupplier, showTitle, index);
  }

  /**
   * Retrieves the registry name of this category which is used in the {@link ItemRegistry} to
   * identify this category.
   *
   * @return The non-null registry name of this category
   */
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  /**
   * Retrieves the display name which is used when rendering the category in the client.
   *
   * @return The non-null display name of this category
   */
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * Retrieves the index which is used to sort the categories in the client.
   *
   * @return The index of this category
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * Retrieves the icon which is used when rendering the category in the client.
   *
   * @return The new non-null icon of this category
   */
  public ItemType createIcon() {
    return this.iconSupplier.get();
  }

  /**
   * Retrieves whether the title of this category will be rendered in the creative menu by the
   * client.
   *
   * @return {@code true} if the title should be rendered, {@code false} otherwise
   */
  public boolean isTitleShown() {
    return this.showTitle;
  }
}
