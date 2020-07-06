package net.labyfy.component.security;

import java.security.Permission;

/**
 * Interface to a very basic, yet complete security component restricting the
 * permissions of code with an unknown origin.
 *
 * Normal packages wont be restricted on their actions by default, the security manager
 * is in fact intended to be used by packages which load extern content.
 */
public interface LabyfySecurityManager {
  /**
   * Checks if the currently execution code has the given permission.
   *
   * @param permission The permission to check for
   * @throws SecurityException If the currently executing code does not have the supplied permission
   */
  void checkPermission(Permission permission) throws SecurityException;

  /**
   * Adds a new security checker which will be used for testing if code
   * has the required permissions.
   *
   * @param checker The checker to install
   * @throws SecurityException If the currently executing code is not allowed to install new checkers
   */
  void installChecker(PermissionChecker checker);
}
