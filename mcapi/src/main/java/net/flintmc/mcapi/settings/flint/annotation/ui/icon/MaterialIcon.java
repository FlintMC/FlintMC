package net.flintmc.mcapi.settings.flint.annotation.ui.icon;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An icon which displays an item stack with a specified type, amount an enchantment.
 *
 * @see Icon
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialIcon {

  /**
   * Retrieves the type of the item to be displayed, for example 'minecraft:stone'.
   *
   * @return The type of the item
   */
  String value();

  /**
   * Retrieves the amount of items on the given stack to be displayed.
   *
   * @return The amount of items
   */
  int amount() default 1;

  /**
   * Retrieves whether the displayed item should be enchanted or not.
   *
   * @return {@code true} if the displayed item should be enchanted, {@code false} otherwise
   */
  boolean enchanted() default false;
}
