package net.flintmc.transform.hook;

import net.flintmc.transform.hook.Hook.ExecutionTime;

/**
 * Defines a specific result after a {@link Hook} has been called.
 *
 * @see Hook
 */
public enum HookResult {

  /**
   * Breaks further execution in the annotated method, this only works in {@link
   * ExecutionTime#BEFORE} since in {@link ExecutionTime#AFTER} there is nothing more to be broken.
   */
  BREAK,

  /**
   * Does nothing more after calling the method in the hook. Same result as if {@code null} is
   * returned by the hook and the return type is not {@link HookResult}.
   */
  CONTINUE
}
