package net.flintmc.mcapi.settings.flint.annotation.ui.icon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

/**
 * Can be used with any {@link ApplicableSetting} to add an icon to the setting. If it is not set,
 * there will be no icon. Only one of the values in this annotation should be set.
 *
 * <p>The resulting json from the {@link SettingsSerializationHandler} will contain an 'icon' object
 * with the data from the provided value.
 *
 * <p>For the item, it will contain a 'type', 'amount' and 'enchanted' containing the values from
 * the item annotation, for the url it will contain a 'url' string, for the html it will contain a
 * 'html' string and for the resource a URL will be generated and added to the object as a 'url'
 * string.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Icon {

  /**
   * Retrieves information about the item that should be displayed or a MaterialIcon with an empty
   * value to display no item.
   *
   * @return The item to be displayed
   */
  MaterialIcon item() default @MaterialIcon("");

  /**
   * Retrieves the path to the resource that should be displayed. For example
   * "minecraft:textures/particle/particles.png" to get the file from
   * "assets/minecraft/textures/particle/particles.png" from the resource packs.
   *
   * @return The path to the resource
   */
  String resource() default "";

  /**
   * Retrieves the URL to get the image from to be displayed or an empty string if no URL should be
   * displayed.
   *
   * @return The URL to be displayed
   */
  String url() default "";

  /**
   * Retrieves the HTML to be displayed or an empty string if no HTMl should be displayed.
   *
   * @return The HTML to be displayed
   */
  String html() default "";
}
