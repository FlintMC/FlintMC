package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtField;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s located at field level.
 *
 * @see Identifier
 */
public class FieldIdentifier implements Identifier<CtField> {

  private final String owner;
  private final String name;

  public FieldIdentifier(String owner, String name) {
    this.owner = owner;
    this.name = name;
  }

  /**
   * @return The class name of the declaring class of the field represented by this identifier
   */
  public String getOwner() {
    return this.owner;
  }

  /**
   * @return The field name of this identifier
   */
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtField getLocation() {
    try {
      return ClassPool.getDefault()
          .get(this.getOwner())
          .getDeclaredField(this.getName());
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
