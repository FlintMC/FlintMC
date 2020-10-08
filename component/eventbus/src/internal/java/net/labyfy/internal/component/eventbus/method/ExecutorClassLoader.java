package net.labyfy.internal.component.eventbus.method;

/**
 * A {@link ClassLoader} with a method exposed to define as class.
 */
public class ExecutorClassLoader extends ClassLoader {

  /**
   * Creates a new class loader using the specified parent class loader for delegation.
   *
   * @param parent The parent classloader.
   */
  protected ExecutorClassLoader(final ClassLoader parent) {
    super(parent);
  }

  /**
   * Converts an array of bytes into an instance of {@link Class}. Before the {@link Class} can be used it must
   * be resolved.
   *
   * @param name  The expected binary name of the class, or {@code null} if not known.
   * @param bytes The bytes that make up the class data.
   * @param <T>   The type of the class.
   * @return The {@link Class} object that was created from the specified class data.
   */
  @SuppressWarnings("unchecked")
  <T> Class<T> defineClass(final String name, final byte[] bytes) {
    return (Class<T>) this.defineClass(name, bytes, 0, bytes.length);
  }
}
