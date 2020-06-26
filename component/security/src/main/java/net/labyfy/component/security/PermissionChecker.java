package net.labyfy.component.security;

import java.security.Permission;
import java.util.List;

/**
 * Interface for additional permission checks installable on the {@link LabyfySecurityManager}.
 */
public interface PermissionChecker {
  /**
   * Called by the current {@link LabyfySecurityManager} every time a check is required.
   *
   * @param callStack  The current call stack
   * @param permission The permission which should be checked
   * @throws SecurityException If the currently execution code does not have the given permission
   */
  void check(List<Class<?>> callStack, Permission permission) throws SecurityException;
}
