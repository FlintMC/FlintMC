package net.labyfy.component.items.meta;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.meta.enchantment.Enchantment;
import net.labyfy.component.items.meta.enchantment.EnchantmentType;
import net.labyfy.component.items.type.ItemType;

import java.util.List;

/**
 * Represents the NBT of an {@link ItemStack}.
 */
public interface ItemMeta {

  /**
   * Retrieves the display name of this meta which will be displayed instead of the default name of the item.
   *
   * @return The custom display name or {@code null} if this meta doesn't have any custom display name
   */
  ChatComponent getCustomDisplayName();

  /**
   * Sets the display name of this meta which will be displayed instead of the default name of the item.
   *
   * @param displayName The new display name of this meta or {@code null} to remove the custom display name
   */
  void setCustomDisplayName(ChatComponent displayName);

  /**
   * Retrieves the lines in the lore of this meta which will be displayed below the display name.
   *
   * @return The non-null lore with non-null components
   */
  ChatComponent[] getLore();

  /**
   * Sets the lore of this meta which will be displayed below the display name.
   *
   * @param lore The non-null lore with non-null components
   */
  void setLore(ChatComponent... lore);

  /**
   * Sets the lore of this meta which will be displayed below the display name.
   *
   * @param lore The non-null lore with non-null components
   */
  void setLore(List<ChatComponent> lore);

  /**
   * Retrieves the remaining durability of this meta. The remaining durability is the result of the max damage of the
   * {@link ItemType} of this meta minus the current {@link #getDamage()}.
   *
   * @return The remaining durability or -1 if this item is not damageable
   */
  int getRemainingDurability();

  /**
   * Sets the remaining durability of this meta. The remaining durability is the result of the max damage of the {@link
   * ItemType} of this meta minus the current {@link #getDamage()}.
   *
   * @param remainingDurability The new remaining durability for this meta
   * @throws IllegalStateException    If the item of this meta is not damageable
   * @throws IllegalArgumentException If max damage - {@code remainingDurability} is smaller than 0 or greater than the
   *                                  max damage of the {@link ItemType} of this meta
   */
  void setRemainingDurability(int remainingDurability) throws IllegalStateException, IllegalArgumentException;

  /**
   * Retrieves the amount of damage that has been done to the item of this meta.
   *
   * @return The damage of this meta or -1 if the item of this meta is not damageable
   */
  int getDamage();

  /**
   * Sets the damage of this meta.
   *
   * @param damage The new damage for this meta in the range from 0 to the max damage of the {@link ItemType} of this
   *               meta
   * @throws IllegalStateException    If the item of this meta is not damageable
   * @throws IllegalArgumentException If the given {@code damage} is smaller than 0 or greater than the max damage of
   *                                  the {@link ItemType} of this meta
   */
  void setDamage(int damage) throws IllegalStateException, IllegalArgumentException;

  /**
   * Retrieves an array of all enchantments of this meta.
   *
   * @return The non-null array of enchantments on this meta, modification to this array will have no effect
   */
  Enchantment[] getEnchantments();

  /**
   * Retrieves whether this meta is enchanted with the given type
   *
   * @param type The non-null type to check for
   * @return {@code true} if it is enchanted with this meta or {@code false} otherwise
   */
  boolean hasEnchantment(EnchantmentType type);

  /**
   * Adds the given enchantment to this meta if it is not already added.
   *
   * @param enchantment The new non-null enchantment
   * @throws IllegalArgumentException If an enchantment with the given type is already added to this meta
   * @see #hasEnchantment(EnchantmentType)
   * @see EnchantmentType#createEnchantment(int)
   */
  void addEnchantment(Enchantment enchantment) throws IllegalArgumentException;

  /**
   * Removes an enchantment with the given type out of this meta if it is added. Nothing will happen if this meta isn't
   * enchanted with the given type.
   *
   * @param type The non-null type to be removed
   */
  void removeEnchantment(EnchantmentType type);

  /**
   * Retrieves an enchantment by the given out of this meta. If an enchantment is returned, the type in this enchantment
   * will match the given type in this method.
   *
   * @param type The non-null type to get the enchantment for
   * @return The enchantment of this meta or {@code null} if this meta isn't enchanted with the given type
   */
  Enchantment getEnchantment(EnchantmentType type);

  /**
   * Fills this meta from the given NBT compound. This method is only intended to be used internally.
   *
   * @param source The non-null NBT compound tag which should be applied to this meta
   * @throws IllegalArgumentException If the given object is not an instance of the NBT compound for the minecraft
   *                                  version
   */
  void applyNBTFrom(Object source) throws IllegalArgumentException;

  /**
   * Copies the NBT of this meta into the given NBT compound. This method is only intended to be used internally.
   *
   * @param target The non-null NBT compound tag to copy the NBT data to
   * @throws IllegalArgumentException If the given object is not an instance of the NBT compound for the minecraft
   *                                  version
   */
  void copyNBTTo(Object target);

  /**
   * Factory for {@link ItemMeta}.
   */
  interface Factory {

    /**
     * Creates a new item meta for the given type.
     *
     * @param type The non-null type to create the meta with
     * @return The new non-null meta for the given type
     */
    ItemMeta createMeta(ItemType type);

  }

}
