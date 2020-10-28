package net.labyfy.component.config.annotation;

import net.labyfy.component.config.storage.ConfigStorage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Acts as a blacklist for {@link ConfigStorage}s inside a {@link Config}, more information can be found in the {@link
 * Config}.
 * <p>
 * The opposite of {@link IncludeStorage} (exclude has a higher priority).
 *
 * @see Config
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ExcludeStorage {

  /**
   * An array of storages that should be excluded, empty for no excluded storage. If the array is empty, it has the same
   * effect as if the annotation would just not be specified.
   * <p>
   * For example "local" as the value would specify that the value would be stored in every storage except for the
   * default Labyfy storage.
   *
   * @return The non-null array of storages to be excluded
   */
  String[] value();

}
