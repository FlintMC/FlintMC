package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

import java.util.function.BiFunction;

public class ConstructorIdentifier implements Identifier<CtConstructor> {

  private final String owner;
  private final String[] parameters;
  private BiFunction<ConstructorIdentifier, String, String> ownerConverter = (constructorIdentifier, owner) -> owner;
  private BiFunction<ConstructorIdentifier, String[], String[]> parametersConverter = (constructorIdentifier, parameters) -> parameters;

  public ConstructorIdentifier(String owner, String... parameters) {
    this.owner = owner;
    this.parameters = parameters;
  }

  public String getOwner() {
    return this.ownerConverter.apply(this, this.owner);
  }

  public String[] getParameters() {
    return this.parametersConverter.apply(this, this.parameters);
  }

  public ConstructorIdentifier setParametersConverter(BiFunction<ConstructorIdentifier, String[], String[]> parametersConverter) {
    this.parametersConverter = parametersConverter;
    return this;
  }

  public ConstructorIdentifier setOwnerConverter(BiFunction<ConstructorIdentifier, String, String> ownerConverter) {
    this.ownerConverter = ownerConverter;
    return this;
  }

  @Override
  public CtConstructor getLocation() {
    try {
      return ClassPool.getDefault().get(this.getOwner()).getDeclaredConstructor(ClassPool.getDefault().get(this.getParameters()));
    } catch (NotFoundException exception) {
      throw new IllegalStateException(exception);
    }
  }
}
