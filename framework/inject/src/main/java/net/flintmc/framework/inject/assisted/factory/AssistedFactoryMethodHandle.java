package net.flintmc.framework.inject.assisted.factory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AssistedFactoryMethodHandle {

  private static final int MODIFIERS =
      Modifier.PRIVATE | Modifier.STATIC | Modifier.PUBLIC | Modifier.PROTECTED;
  private final Constructor<MethodHandles.Lookup> lookupConstructor;

  /**
   * Constructs a new assisted factory method handle.
   */
  protected AssistedFactoryMethodHandle() {
    this.lookupConstructor = this.findConstructorHandlesLookup();
  }

  /**
   * Creates a {@link MethodHandle} with the given parameters.
   *
   * @param method The method to creates a constructor.
   * @param proxy  The proxy of the method.
   * @return A created method handle or {@code null}.
   */
  public MethodHandle createMethodHandle(Method method, Object proxy) {
    if (this.lookupConstructor == null) {
      return null;
    }

    Class<?> declaringClass = method.getDeclaringClass();

    try {
      MethodHandles.Lookup lookup = this.lookupConstructor.newInstance(declaringClass, MODIFIERS);
      method.setAccessible(true);

      return lookup.unreflectSpecial(method, declaringClass).bindTo(proxy);
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException("Unable to access method: " + method, exception);
    }

  }

  /**
   * @see MethodHandles.Lookup
   */
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
