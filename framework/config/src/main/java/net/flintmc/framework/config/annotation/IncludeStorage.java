package net.flintmc.framework.config.annotation;

import net.flintmc.framework.config.storage.ConfigStorage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Acts as a whitelist for {@link ConfigStorage}s inside a {@link Config}, more information can be found in the {@link
 * Config}.
 * <p>
 * The opposite of {@link ExcludeStorage} (exclude has a higher priority).
 *
 * @see Config
 * @see ExcludeStorage
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IncludeStorage {

  /**
   * An array of storages that should be included, empty to include all storages. If the array is empty, it has the same
   * effect as if the annotation would just not be specified.
   * <p>
   * For example "local" as the value would specify that the value would be only stored in the default FlintMC storage.
   *
   * @return The non-null array of storages to be included
   */
  String[] value();

}
