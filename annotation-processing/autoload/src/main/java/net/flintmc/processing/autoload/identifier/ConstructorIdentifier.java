package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ConstructorIdentifier implements Identifier<CtConstructor> {

  private final String owner;
  private final String[] parameters;

  public ConstructorIdentifier(String owner, String... parameters) {
    this.owner = owner;
    this.parameters = parameters;
  }

  public String getOwner() {
    return this.owner;
  }

  public String[] getParameters() {
    return this.parameters;
  }

  @Override
  public CtConstructor getLocation() {
    try {
      return ClassPool.getDefault().get(this.getOwner())
          .getDeclaredConstructor(ClassPool.getDefault().get(this.getParameters()));
    } catch (NotFoundException exception) {
      throw new IllegalStateException(exception);
    }
  }
}
