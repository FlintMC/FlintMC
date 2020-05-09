package net.labyfy.component.gui.mcjfxgl.component.theme.source;

import net.labyfy.component.launcher.classloading.common.CommonClassLoader;
import net.labyfy.component.security.PermissionChecker;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.List;
import java.util.PropertyPermission;

public class ThemePermissionChecker implements PermissionChecker {
  @Override
  public void check(List<Class<?>> callStack, Permission permission) throws SecurityException {
    for(Class<?> clazz : callStack) {
      if(CommonClassLoader.class.isAssignableFrom(clazz) && permission instanceof FilePermission) {
        return;
      }

      if(clazz.getProtectionDomain().getCodeSource() instanceof ThemeCodeSource) {
        if(permission instanceof RuntimePermission) {
          checkRuntime((RuntimePermission) permission);
        } else if(permission instanceof ReflectPermission) {
          checkReflect((ReflectPermission) permission);
        } else if(permission instanceof PropertyPermission) {
          checkProperty((PropertyPermission) permission);
        } else {
          fail(permission);
        }
      }
    }
  }

  private void checkRuntime(RuntimePermission permission) {
    if(permission.getName().startsWith("accessClassInPackage.")) {
      checkPackageAccess(permission.getName().substring(21));
      return;
    }

    switch (permission.getName()) {
      case "accessDeclaredMembers":
      case "createClassLoader":
      case "getClassLoader":
      case "getProtectionDomain":
        break;

      default:
        fail(permission);
    }
  }

  private void checkPackageAccess(String pkg) {
    if(
        !pkg.startsWith("com.sun.javafx") &&
        !pkg.startsWith("javafx") &&
        !pkg.startsWith("com.sun.glass")) {
      throw new SecurityException("Theme scripts are not allowed to reflectively access classes in " + pkg);
    }
  }

  private void checkReflect(ReflectPermission permission) {
    if(!permission.getName().equals("suppressAccessChecks")) {
      fail(permission);
    }
  }

  private void checkProperty(PropertyPermission permission) {
    if (!permission.getName().equals("java.verbose") || !permission.getActions().equals("read")) {
      throw new SecurityException("Theme scripts are not allowed to access the property " + permission.getName());
    }
  }

  private void fail(Permission permission) {
    throw new SecurityException("Theme scripts don't have the permission " + permission.toString());
  }
}
