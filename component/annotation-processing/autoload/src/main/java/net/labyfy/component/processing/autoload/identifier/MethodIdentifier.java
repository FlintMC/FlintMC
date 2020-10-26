package net.labyfy.component.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Implements an {@link Identifier} to locate {@link
 * net.labyfy.component.processing.autoload.DetectableAnnotation}s located at method level.
 *
 * @see Identifier
 */
public class MethodIdentifier implements Identifier<CtMethod> {
  private final String owner;
  private final String name;
  private final String[] parameters;

  public MethodIdentifier(String owner, String name, String... parameters) {
    this.owner = owner;
    this.name = name;
    this.parameters = parameters;
  }

  /**
   * @return The class name of the declaring class of the method represented by this identifier
   */
  public String getOwner() {
    return owner;
  }

  /** @return The parameter type names of the method represented by this identifier */
  public String[] getParameters() {
    return parameters;
  }

  /** @return The method name of this identifier */
  public String getName() {
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod getLocation() {
    try {
      return ClassPool.getDefault()
          .get(this.owner)
          .getDeclaredMethod(this.name, ClassPool.getDefault().get(this.parameters));
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
