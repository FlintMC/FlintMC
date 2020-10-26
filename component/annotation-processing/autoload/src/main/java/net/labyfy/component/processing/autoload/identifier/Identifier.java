package net.labyfy.component.processing.autoload.identifier;

/**
 * Represents the location where a {@link
 * net.labyfy.component.processing.autoload.DetectableAnnotation} is placed at.
 *
 * @param <T> Object representation type of this {@link Identifier}
 * @see MethodIdentifier
 * @see ClassIdentifier
 */
public interface Identifier<T> {

  /**
   * @return The object representation ot this {@link Identifier}
   */
  T getLocation();
}
