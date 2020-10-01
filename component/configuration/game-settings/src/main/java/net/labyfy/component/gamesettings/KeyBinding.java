package net.labyfy.component.gamesettings;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents a key binding.
 */
public interface KeyBinding {

  /**
   * Retrieves the key category of the key binding.
   *
   * @return The key category.
   */
  String getKeyCategory();

  /**
   * Retrieves the key description of the key binding.
   *
   * @return The key description.
   */
  String getKeyDescription();

  /**
   * Retrieves the key code of the key binding.
   *
   * @return The key code.
   */
  int getKeyCode();

  /**
   * Whether the key binding is pressed.
   *
   * @return {@code true} if the key binding is pressed.
   */
  boolean isPressed();

  /**
   * Changes the state whether the key binding is pressed.
   *
   * @param pressed The new state.
   */
  void setPressed(boolean pressed);

  /**
   * Binds the key binding to a key.
   *
   * @param keyBind The new key for the binding.
   */
  void bind(KeyBindMappings keyBind);

  /**
   * Whether the key binding is invalid.
   *
   * @return {@code true} if the key binding is invalid, otherwise {@code false}.
   */
  boolean isInvalid();

  /**
   * Checks if the given parameters match the values in the key binding.
   *
   * @param keyCode  The key code.
   * @param scanCode The scan code.
   * @return {@code true} if the given parameters match the values, otherwise {@code false}.
   */
  boolean matchesKey(int keyCode, int scanCode);

  /**
   * Checks if the given parameter match the value in the key binding.
   *
   * @param key The mouse key.
   * @return {@code true} if the given parameters match the values, otherwise {@code false}.
   */
  boolean matchesMouseKey(int key);

  /**
   * Retrieves the localized name of this key binding.
   *
   * @return The localized name.
   */
  String getLocalizedName();

  /**
   * Whether the key binding is default.
   *
   * @return {@code true} if the key binding is default, otherwise {@code false}.
   */
  boolean isDefault();

  /**
   * Retrieves the translation key of the key binding.
   *
   * @return The translation key of the key binding.
   */
  String getTranslationKey();

  @AssistedFactory(KeyBinding.class)
  interface Factory {

    KeyBinding create(
            @Assisted("description") String description,
            @Assisted("keyCode") int keyCode,
            @Assisted("category") String category
    );
  }

}
