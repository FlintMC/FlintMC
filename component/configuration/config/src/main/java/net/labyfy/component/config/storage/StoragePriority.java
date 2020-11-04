package net.labyfy.component.config.storage;

import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a storage to be automatically registered in the {@link ConfigStorageProvider}. If a storage is manually
 * registered, this annotation is still necessary to set the priority.
 * <p>
 * This annotation is to be used on classes that implement {@link ConfigStorage}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface StoragePriority {

  /**
   * Retrieves the priority of the underlying storage. The lower the value (the absolute minimum is {@link
   * Integer#MIN_VALUE}), the later the read method will be called by {@link ConfigStorageProvider#read(ParsedConfig)}
   * and therefore the lower the chance of the read values not being overridden is.
   * <p> <br>
   * The "normal" priority is {@code 0}.
   *
   * @return The priority which can be any int
   */
  int value() default 0;

}
