package net.labyfy.component.entity.name;

import net.labyfy.chat.component.TextComponent;

/**
 * Serves as name interface for objects.
 */
public interface Nameable {

  /**
   * Retrieves the name of the object.
   *
   * @return The object name.
   */
  TextComponent getName();

  /**
   * Whether the object has a custom name.
   *
   * @return {@code true} if the object has a custom name, otherwise {@code false}.
   */
  default boolean hasCustomName() {
    return this.getCustomName() != null;
  }

  /**
   * Retrieves the display name of the object.
   *
   * @return The object display name.
   */
  default TextComponent getDisplayName() {
    return this.getName();
  }

  /**
   * Retrieves the custom name of the object.
   *
   * @return The object custom name.
   */
  default TextComponent getCustomName() {
    return null;
  }

}
