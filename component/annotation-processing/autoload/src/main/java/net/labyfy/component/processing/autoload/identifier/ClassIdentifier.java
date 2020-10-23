package net.labyfy.component.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassIdentifier implements Identifier<CtClass> {
  private final String name;

  public ClassIdentifier(String name) {
    this.name = name;
  }

  @Override
  public CtClass getLocation() {
    try {
      return ClassPool.getDefault().get(this.name);
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  public String getName() {
    return name;
  }
}
