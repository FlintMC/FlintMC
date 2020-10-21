package net.labyfy.component.session.launcher.serializer;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a {@link LauncherProfileSerializer} with a version to be automatically registered in the {@link
 * LauncherProfileSerializer}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface ProfileSerializerVersion {

  /**
   * Retrieves the version of the serializer, the versions cannot have duplicates per Injector.
   *
   * @return The version of the serializer
   */
  int value();

}
