package net.flintmc.mcapi.settings.flint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;

/**
 * Marks an annotation to be used to define a setting like {@link BooleanSetting}, {@link
 * SliderSetting}, ...
 *
 * <p>A config with settings could look like the following:
 *
 * <pre>
 *    {@literal @}Config
 *     public interface MySettings {
 *
 *       {@literal @}DisplayName({@literal @}Component("My string value")) // set an optional displayName
 *       {@literal @}Description({@literal @}Component("Description of my string value")) // set an optional description
 *       {@literal @}StringSetting // define the value of this method to be a string setting
 *       {@literal @}DefineCategory( // define a new category and apply it to this setting
 *                         // - if the category already exists, nothing will happen
 *                         // - if you know that the category already exists, just use {@literal @}Category
 *          name = "my unique category",
 *          displayName = {@literal @}Component("My category"),
 *          description = {@literal @}Component("This is a category")
 *        )
 *       {@literal @}SubCategory({@literal @}Component("This is a splitter")) // set an optional sub category
 *       {@literal @}DefaultString("default value of this setting") // set the default, if not set this will be null
 *                                                              // for primitives it will be 0 for numbers,
 *                                                              // false for booleans and '\0' for characters
 *        String getValue();
 *
 *       {@literal @}SubSettingsFor("Value") // marks every setting in the SubSettings interface
 *                                 // to be sub settings of the setting on 'getValue'
 *        interface SubSettings {
 *
 *          {@literal @}BooleanSetting
 *           boolean isSomeSettingEnabled()
 *
 *        }
 *
 *     }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ApplicableSetting {

  /**
   * Retrieves the unique name of this setting to identify it in the serialized version.
   *
   * @return The unique name of this setting
   */
  String name();

  /**
   * Retrieves all applicable types for this setting that can be used as a return type in a {@link
   * Config}.
   *
   * <p>{@link ConfigObjectReference#getSerializedType()} (or if it is a {@link Map}, the value
   * type
   * of it) has to be assignable to at least one of these types.
   *
   * @return The types for this setting
   */
  Class<?>[] types();
}
