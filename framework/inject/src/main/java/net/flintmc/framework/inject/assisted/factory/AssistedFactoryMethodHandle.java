package net.flintmc.framework.inject.assisted.factory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AssistedFactoryMethodHandle {

  private final Constructor<MethodHandles.Lookup> lookupConstructor;

  public AssistedFactoryMethodHandle() {
    this.lookupConstructor = this.findConstructorHandlesLookup();
  }

  public MethodHandle createMethodHandle(Method method, Object proxy) {
    if (this.lookupConstructor == null) {
      return null;
    }

    Class<?> declaringClass = method.getDeclaringClass();

    int modifiers = Modifier.PRIVATE | Modifier.STATIC | Modifier.PUBLIC | Modifier.PROTECTED;

    try {
      MethodHandles.Lookup lookup = this.lookupConstructor.newInstance(declaringClass, modifiers);
      method.setAccessible(true);

      return lookup.unreflectSpecial(method, declaringClass).bindTo(proxy);
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException("Unable to access method: " + method, exception);
    }

  }

  private Constructor<MethodHandles.Lookup> findConstructorHandlesLookup() {
    try {
      Constructor<MethodHandles.Lookup> constructorLookup =
              MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
      constructorLookup.setAccessible(true);
      return constructorLookup;
    } catch (ReflectiveOperationException ignored) {
      return null;
    }
  }


}
