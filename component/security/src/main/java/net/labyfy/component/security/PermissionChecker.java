package net.labyfy.component.security;

import java.security.Permission;
import java.util.List;

public interface PermissionChecker {
  void check(List<Class<?>> callStack, Permission permission) throws SecurityException;
}
