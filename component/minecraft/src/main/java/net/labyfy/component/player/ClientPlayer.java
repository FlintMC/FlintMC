package net.labyfy.component.player;

import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.overlay.TabOverlay;

public interface ClientPlayer extends PlayerSkinProfile, AbstractClientPlayer {

  /**
   * Retrieves the player entity.
   *
   * @return The client player entity.
   */
  PlayerEntity getEntity();

  /**
   * Retrieves the inventory of this player.
   *
   * @return The player's inventory.
   */
  PlayerInventory getInventoryController();

  /**
   * Retrieves the opened inventory of this player.
   *
   * @return The opened inventory.
   */
  Inventory getOpenInventory();

  /**
   * Prints a message into the player chat.
   *
   * @param message The message to print.
   */
  void sendChatMessage(String message);

  /**
   * Closes the screen and drop an item stack.
   */
  void closeScreenAndDropStack();

  /**
   * Changes the health of this player.
   * <p>
   * This is only on the client side.
   *
   * @param health The new health.
   */
  void setPlayerSPHealth(float health);

  /**
   * Sends the horse inventory to the server.
   */
  void sendHorseInventory();

  /**
   * Retrieves the server brand
   *
   * @return The server brand.
   */
  String getServerBrand();

  /**
   * Changes the server brand.
   *
   * @param serverBrand The new server brand.
   */
  void setServerBrand(String serverBrand);

  /**
   * Changes the permission level of this player.
   *
   * @param level The new permission level.
   */
  void setPermissionLevel(int level);

  /**
   * Sets the experience stats of this player.
   *
   * @param currentExperience The current experience of this player.
   * @param maxExperience     The maximal experience of this player.
   * @param level             The level of this player.
   */
  void setExperienceStats(float currentExperience, int maxExperience, int level);

  /**
   * Whether the player is show a death screen.
   *
   * @return {@code true} if the player is show a death screen, otherwise {@code false}.
   */
  boolean isShowDeathScreen();

  /**
   * Changes the state whether the death screen is displayed.
   *
   * @param showDeathScreen {@code true} if the death screen should be displayed, otherwise {@code false}.
   */
  void setShowDeathScreen(boolean showDeathScreen);

  /**
   * Whether the player is riding a horse.
   *
   * @return {@code true} if the player is riding a horse, otherwise {@code false}.
   */
  boolean isRidingHorse();

  /**
   * Retrieves the horse jump power.
   *
   * @return The jump power of the horse.
   */
  float getHorseJumpPower();

  /**
   * Whether the player is rowing with a boat.
   *
   * @return {@code true} if the player is rowing with a boat, otherwise {@code false}.
   */
  boolean isRowingBoat();

  /**
   * Whether auto jump is enabled.
   *
   * @return {@code true} if the auto jump is enabled, otherwise {@code false}.
   */
  boolean isAutoJumpEnabled();

  /**
   * Retrieves the water brightness of this player.
   *
   * @return The water brightness.
   */
  float getWaterBrightness();

  /**
   * Retrieves the tab overlay of this player.
   *
   * @return The tab overlay.
   */
  TabOverlay getTabOverlay();

}
