package net.labyfy.component.entity.item;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;

import java.util.UUID;

/**
 * Represents the Minecraft item entity.
 */
public interface ItemEntity extends Entity {

  /**
   * Retrieves the entity as an item stack.
   *
   * @return The entity as an item stack.
   */
  ItemStack getItemStack();

  /**
   * Changes the item stack of this entity.
   *
   * @param itemStack The new item stack.
   */
  void setItemStack(ItemStack itemStack);

  /**
   * Retrieves the identifier of the item entity owner.
   *
   * @return The identifier of the item entity owner.
   */
  UUID getOwnerIdentifier();

  /**
   * Changes the owner identifier.
   *
   * @param ownerIdentifier The new owner identifier.
   */
  void setOwnerIdentifier(UUID ownerIdentifier);

  /**
   * Retrieves the identifier of the item entity thrower.
   *
   * @return The identifier of the item entity thrower.
   */
  UUID getThrowerIdentifier();

  /**
   * Changes the thrower identifier.
   *
   * @param throwerIdentifier The new thrower identifier.
   */
  void setThrowerIdentifier(UUID throwerIdentifier);

  /**
   * Retrieves the age of this item entity.
   *
   * @return The age of this item entity.
   */
  int getAge();

  /**
   * Sets that the item entity has a default pickup delay.
   */
  void setDefaultPickupDelay();

  /**
   * Sets that the item entity has not pickup delay.
   */
  void setNoPickupDelay();

  /**
   * Sets that the item entity has an infinite pickup delay.
   */
  void setInfinitePickupDelay();

  /**
   * Changes the pickup delay of this item entity.
   *
   * @param ticks The new pickup delay.
   */
  void setPickupDelay(int ticks);

  /**
   * Whether the item entity cannot be picked up.
   *
   * @return {@code true} if the item entity cannot be picked up, otherwise {@code false}.
   */
  boolean cannotPickup();

  /**
   * Sets that the item cannot despawn.
   */
  void setNoDespawn();

  /**
   * Make the entity a fake item.
   */
  void makeFakeItem();

  /**
   * A factory class for the {@link ItemEntity}.
   */
  @AssistedFactory(ItemEntity.class)
  interface Factory {

    /**
     * Creates a new {@link ItemEntity} with the given entity.
     *
     * @param entity The entity.
     * @return A created item entity.
     */
    ItemEntity create(@Assisted("entity") Object entity);

    /**
     * Creates a new {@link ItemEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param x      The x position of the entity.
     * @param y      The y position of the entity.
     * @param z      The z position of the entity.
     * @return A created item entity.
     */
    ItemEntity create(
            @Assisted("entity") Object entity,
            @Assisted("x") double x,
            @Assisted("y") double y,
            @Assisted("z") double z
    );

    /**
     * Creates a new {@link ItemEntity} with the given parameters.
     *
     * @param entity    The entity.
     * @param x         The x position of the entity.
     * @param y         The y position of the entity.
     * @param z         The z position of the entity.
     * @param itemStack The item stack for the entity.
     * @return A created item entity.
     */
    ItemEntity create(
            @Assisted("entity") Object entity,
            @Assisted("x") double x,
            @Assisted("y") double y,
            @Assisted("z") double z,
            @Assisted("itemStack") ItemStack itemStack
    );

  }

}
