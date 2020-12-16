package net.flintmc.mcapi.settings.flint.annotation.version;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * Marks an {@link ApplicableSetting} in a {@link Config} to be not used in the specified versions.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface VersionExclude {

  /**
   * Retrieves the versions where the setting should not be accessible. The version can be for
   * example "1.15.2.
   *
   * @return The versions where the settings cannot be used
   */
  String[] value();
}
