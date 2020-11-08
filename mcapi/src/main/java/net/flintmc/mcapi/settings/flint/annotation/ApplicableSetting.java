package net.flintmc.mcapi.settings.flint.annotation;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Marks an annotation to be used to define a setting like {@link BooleanSetting}, {@link
 * SliderSetting}, ...
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
   * <p>{@link ConfigObjectReference#getSerializedType()} (or if it is a {@link Map}, the value type
   * of it) has to be assignable to at least one of these types.
   *
   * @return The types for this setting
   */
  Class<?>[] types();
}
