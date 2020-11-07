package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ConstructorIdentifier implements Identifier<CtConstructor>{

  private final String owner;
  private final CtClass[] parameters;

  public ConstructorIdentifier(String owner, String... parameters) {
    this.owner = owner;
    this.parameters = new CtClass[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      try {
        this.parameters[i] = ClassPool.getDefault().get(parameters[i]);
      } catch (NotFoundException exception) {
        throw new IllegalStateException(exception);
      }
    }
  }

  @Override
  public CtConstructor getLocation() {
    try {
      return ClassPool.getDefault().get(this.owner).getDeclaredConstructor(this.parameters);
    } catch (NotFoundException exception) {
      throw new IllegalStateException(exception);
    }
  }
}
