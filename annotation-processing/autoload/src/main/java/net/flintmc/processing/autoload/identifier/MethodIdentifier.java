package net.flintmc.processing.autoload.identifier;

import com.google.common.base.Functions;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s located at method level.
 *
 * @see Identifier
 */
public class MethodIdentifier implements Identifier<CtMethod> {
  private final String owner;
  private final String name;
  private final String[] parameters;
  private BiFunction<MethodIdentifier, String, String> ownerConverter = (methodIdentifier, owner) -> owner;
  private BiFunction<MethodIdentifier, String, String> nameConverter = (methodIdentifier, name) -> name;
  private BiFunction<MethodIdentifier, String[], String[]> parametersConverter = ((methodIdentifier, parameters) -> parameters);

  public MethodIdentifier(String owner, String name, String... parameters) {
    this.owner = owner;
    this.name = name;
    this.parameters = parameters;
  }

  /**
   * @return The class name of the declaring class of the method represented by this identifier
   */
  public String getOwner() {
    return this.ownerConverter.apply(this, this.owner);
  }

  /**
   * @return The parameter type names of the method represented by this identifier
   */
  public String[] getParameters() {
    return this.parametersConverter.apply(this, this.parameters);
  }

  /**
   * @return The method name of this identifier
   */
  public String getName() {
    return this.nameConverter.apply(this, this.name);
  }

  public MethodIdentifier setNameConverter(BiFunction<MethodIdentifier, String, String> nameConverter) {
    this.nameConverter = nameConverter;
    return this;
  }

  public MethodIdentifier setOwnerConverter(BiFunction<MethodIdentifier, String, String> ownerConverter) {
    this.ownerConverter = ownerConverter;
    return this;
  }

  public MethodIdentifier setParametersConverter(BiFunction<MethodIdentifier, String[], String[]> parametersConverter) {
    this.parametersConverter = parametersConverter;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtMethod getLocation() {
    try {
      return ClassPool.getDefault()
          .get(this.getOwner())
          .getDeclaredMethod(this.getName(), ClassPool.getDefault().get(this.getParameters()));
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
