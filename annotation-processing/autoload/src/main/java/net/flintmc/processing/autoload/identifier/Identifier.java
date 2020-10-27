package net.flintmc.processing.autoload.identifier;

import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Represents the location where a {@link DetectableAnnotation} is placed at.
 *
 * @param <T> Object representation type of this {@link Identifier}
 * @see MethodIdentifier
 * @see ClassIdentifier
 */
public interface Identifier<T> {

  /** @return The object representation ot this {@link Identifier} */
  T getLocation();
}
