package net.flintmc.mcapi.player.type;

/** Represents a cooldown tracker */
public interface CooldownTracking {

  /**
   * Whether the item has a cooldown.
   *
   * @param item The item to be checked
   * @return {@code true} if the item has a cooldown, otherwise {@code false}
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  boolean hasCooldown(Object item);

  /**
   * Retrieves the cooldown of the given item.
   *
   * @param item The item to get the cooldown
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *     full tick.
   * @return The cooldown of this given item.
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  float getCooldown(Object item, float partialTicks);

  /**
   * Sets the for the cooldown tracking.
   *
   * @param item The item for setting the cooldown.
   * @param ticks The ticks, how long the cooldown lasts.
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  void setCooldown(Object item, int ticks);

  /**
   * Removes the item from the cooldown tracking.
   *
   * @param item The item to be removed
   */
  // TODO: 05.09.2020 Replaces the Object to Item when the (Item API?) is ready
  void removeCooldown(Object item);
}
