package net.labyfy.webgui;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link WebFileSystemHandler} implementation that should be used by the web gui backend.
 * The annotated class must implement {@link WebFileSystemHandler}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface WebFileSystem {

  /** @return the protocol name of the filesystem. */
  String value();
}
