package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.gamesettings.KeyBindMappings;
import net.labyfy.component.gamesettings.KeyBinding;

import java.util.List;

/**
 * Represents the key binding configuration.
 */
public interface KeyBindingConfiguration {

  /**
   * Retrieves the forward key binding.
   *
   * @return The forward key binding.
   */
  KeyBinding getKeyBindForward();

  /**
   * Retrieves the left key binding.
   *
   * @return The left key binding.
   */
  KeyBinding getKeyBindLeft();

  /**
   * Retrieves the back key binding.
   *
   * @return The back key binding.
   */
  KeyBinding getKeyBindBack();

  /**
   * Retrieves the right key binding.
   *
   * @return The right key binding.
   */
  KeyBinding getKeyBindRight();

  /**
   * Retrieves the jump key binding.
   *
   * @return The jump key binding.
   */
  KeyBinding getKeyBindJump();

  /**
   * Retrieves the sneak key binding.
   *
   * @return The sneak key binding.
   */
  KeyBinding getKeyBindSneak();

  /**
   * Retrieves the sprint key binding.
   *
   * @return The sprint key binding.
   */
  KeyBinding getKeyBindSprint();

  /**
   * Retrieves the key binding to open an inventory.
   *
   * @return The key binding to open an inventory.
   */
  KeyBinding getKeyBindInventory();

  /**
   * Retrieves the key binding to swap hands.
   *
   * @return The key binding to swap hands.
   */
  KeyBinding getKeyBindSwapHands();

  /**
   * Retrieves the key binding to drop items.
   *
   * @return The key binding to drop items.
   */
  KeyBinding getKeyBindDrop();

  /**
   * Retrieves the key binding to use items.
   *
   * @return The key binding to use items.
   */
  KeyBinding getKeyBindUseItem();

  /**
   * Retrieves the key binding to attack.
   *
   * @return The attack key binding.
   */
  KeyBinding getKeyBindAttack();

  /**
   * Retrieves the key binding to pick up a block.
   *
   * @return The key binding to pick up a block.
   */
  KeyBinding getKeyBindPickBlock();

  /**
   * Retrieves the key binding to open the chat.
   *
   * @return The key binding to open the chat.
   */
  KeyBinding getKeyBindChat();

  /**
   * Retrieves the key binding to open the player list.
   *
   * @return The key binding to open the player list.
   */
  KeyBinding getKeyBindPlayerList();

  /**
   * Retrieves the key binding to open the chat to write a command.
   *
   * @return The key binding to open the chat to write a command.
   */
  KeyBinding getKeyBindCommand();

  /**
   * Retrieves the key binding to make a screenshot.
   *
   * @return The key binding to make a screenshot.
   */
  KeyBinding getKeyBindScreenshot();

  /**
   * Retrieves the key binding to toggle the perspective.
   *
   * @return The key binding to toggle the perspective.
   */
  KeyBinding getKeyBindTogglePerspective();

  /**
   * Retrieves the key binding to toggle the smooth camera.
   *
   * @return The key binding to toggle the smooth camera.
   */
  KeyBinding getKeyBindSmoothCamera();

  /**
   * Retrieves the key binding for switching in full screen mode.
   *
   * @return The key binding for switching in  full screen mode.
   */
  KeyBinding getKeyBindFullscreen();

  /**
   * Retrieves the key binding for spectator outlines.
   *
   * @return The key binding for spectator outlines.
   */
  KeyBinding getKeyBindSpectatorOutlines();

  /**
   * Retrieves the key binding to open the advancements gui.
   *
   * @return The key binding to open the advancements gui.
   */
  KeyBinding getKeyBindAdvancements();

  /**
   * Retrieves the key binding to save the toolbar.
   *
   * @return The key binding to save the toolbar.
   */
  KeyBinding getKeyBindSaveToolbar();

  /**
   * Retrieves the key binding to load the toolbar.
   *
   * @return The key binding to load the toolbar.
   */
  KeyBinding getKeyBindLoadToolbar();

  /**
   * Retrieves a collection with all key bindings for the hotbar.
   *
   * @return A collection with all key binding for the hotbar.
   */
  List<KeyBinding> getKeyBindsHotbar();

  /**
   * Retrieves a collection with all registered key bindings.
   *
   * @return A collection with all registered key bindings.
   */
  List<KeyBinding> getKeyBindings();

  /**
   * Changes the binding of a key.
   *
   * @param keyBinding The key binding to change.
   * @param code       The new code for key binding.
   */
  void setKeyBindingCode(KeyBinding keyBinding, KeyBindMappings code);

}
