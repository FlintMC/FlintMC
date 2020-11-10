package net.flintmc.framework.config.internal.generator.method.reference.invoker;

public interface ReferenceInvoker {

  Object getValue(Object instance);

  void setValue(Object instance, Object newValue);
}
