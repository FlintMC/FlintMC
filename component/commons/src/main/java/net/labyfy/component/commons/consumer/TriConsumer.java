package net.labyfy.component.commons.consumer;

/**
 * Utility class for a consumer accept 3 values.
 *
 * @param <A> The type of the first argument
 * @param <B> The type of the second argument
 * @param <C> The type of the third argument
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
  /**
   * Performs the operation given the specified arguments.
   *
   * @param a the first input argument
   * @param b the second input argument
   * @param c the third input argument
   */
  void accept(A a, B b, C c);
}
