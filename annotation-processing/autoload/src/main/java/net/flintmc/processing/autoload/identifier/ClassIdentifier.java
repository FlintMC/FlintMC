package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Implements an {@link Identifier} to locate {@link
 * DetectableAnnotation}s located at class level.
 *
 * @see Identifier
 */
public class ClassIdentifier implements Identifier<CtClass> {
  private final String name;

  public ClassIdentifier(String name) {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass getLocation() {
    try {
      return ClassPool.getDefault().get(this.name);
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  /** @return The class name of this identifier */
  public String getName() {
    return name;
  }
}
