package net.flintmc.framework.eventbus.method;

import javassist.CtMethod;

import java.util.function.Supplier;

/** Factory for {@link Executor}'s that generates code which invokes a specific method. */
public interface ExecutorFactory {

  /**
   * Creates an {@link Executor}.
   *
   * @param method The method to invoke when an event is fired with exactly one parameter which is
   *     the event that has been fired.
   * @return An created executor.
   */
  Supplier<Executor<?>> create(CtMethod method);
}
