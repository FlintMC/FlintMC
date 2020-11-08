package net.flintmc.util.commons.resolve;

import java.util.function.Function;

/**
 * Functional interface for generic resolving.
 *
 * @param <T> The type to resolve
 * @param <R> The type of the resolved value
 */
@FunctionalInterface
public interface Resolver<T, R> extends Function<T, R> {
  /**
   * Resolves the given value to another value.
   *
   * @param t The value to resolve
   * @return The resolved value
   */
  R resolve(T t);

  /**
   * Resolves the given value to another value.
   *
   * @param t The value to resolve
   * @return The resolved value
   */
  @Override
  default R apply(T t) {
    return resolve(t);
  }
}
