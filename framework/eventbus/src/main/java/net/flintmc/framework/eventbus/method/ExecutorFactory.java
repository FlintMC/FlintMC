package net.flintmc.framework.eventbus.method;

import javassist.CtMethod;

/** Factory for {@link EventExecutor}'s that generates code which invokes a specific method. */
public interface ExecutorFactory {

  /**
   * Creates an {@link EventExecutor} or if present uses the cached one.
   *
   * @param method The method to invoke when an event is fired
   * @return A created executor.
   */
  EventExecutor<?> create(CtMethod method);
}
