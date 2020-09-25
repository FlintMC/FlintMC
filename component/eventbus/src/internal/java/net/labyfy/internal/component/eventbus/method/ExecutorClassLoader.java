package net.labyfy.internal.component.eventbus.method;

/**
 * A {@link ClassLoader} with a method exposed to define as class.
 */
public class ExecutorClassLoader extends ClassLoader {

  protected ExecutorClassLoader(final ClassLoader parent) {
    super(parent);
  }

  @SuppressWarnings("unchecked")
  <T> Class<T> defineClass(final String name, final byte[] bytes) {
    return (Class<T>) this.defineClass(name, bytes, 0, bytes.length);
  }
}
