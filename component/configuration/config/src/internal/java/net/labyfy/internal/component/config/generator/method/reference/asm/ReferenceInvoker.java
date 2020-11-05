package net.labyfy.internal.component.config.generator.method.reference.asm;

public interface ReferenceInvoker {

  Object getValue(Object instance);

  void setValue(Object instance, Object newValue);

}
