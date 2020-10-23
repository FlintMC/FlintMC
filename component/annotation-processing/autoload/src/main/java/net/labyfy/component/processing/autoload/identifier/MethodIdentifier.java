package net.labyfy.component.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MethodIdentifier implements Identifier<CtMethod> {
  private final String owner;
  private final String name;
  private final String[] parameters;

  public MethodIdentifier(String owner, String name, String... parameters) {
    this.owner = owner;
    this.name = name;
    this.parameters = parameters;
  }

  public String getOwner() {
    return owner;
  }

  public String[] getParameters() {
    return parameters;
  }

  public String getName() {
    return name;
  }

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
