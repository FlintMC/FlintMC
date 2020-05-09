package net.labyfy.component.gui.mcjfxgl.component.theme.source;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.text.Font;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.launcher.classloading.common.CommonClassLoader;
import net.labyfy.component.security.PermissionChecker;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.security.AllPermission;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;
import java.util.logging.LoggingPermission;

@Singleton
public class ThemePermissionChecker implements PermissionChecker {
  private static final Path JAVA_INSTALLATION_HOME = Paths.get(System.getProperty("java.home"));
  private static final Path TEMPORARY_DIR = Paths.get(System.getProperty("java.io.tmpdir"));

  private final List<Path> themePaths;

  @InjectLogger
  private Logger logger;

  @Inject
  private ThemePermissionChecker() {
    themePaths = new ArrayList<>();
  }

  @Override
  public void check(List<Class<?>> callStack, Permission permission) throws SecurityException {
    try {
      for (Class<?> clazz : callStack) {
        if (CommonClassLoader.class.isAssignableFrom(clazz) && permission instanceof FilePermission) {
          return;
        }

        if (clazz.getProtectionDomain().getCodeSource() instanceof ThemeCodeSource) {
          if (permission instanceof RuntimePermission) {
            checkRuntime((RuntimePermission) permission, callStack);
          } else if (permission instanceof ReflectPermission) {
            checkReflect((ReflectPermission) permission);
          } else if (permission instanceof PropertyPermission) {
            checkProperty((PropertyPermission) permission, callStack);
          } else if (permission instanceof FilePermission) {
            checkFile((FilePermission) permission);
          } else if(permission instanceof AllPermission) {
            checkAll((AllPermission) permission, callStack);
          } else if (!(permission instanceof LoggingPermission)) {
            fail(permission);
          }
        }
      }
    } catch (SecurityException e) {
      logger.trace("Theme script violated security, tried using permission {}", permission.toString());
      throw e;
    }
  }

  private void checkRuntime(RuntimePermission permission, List<Class<?>> callStack) {
    if(permission.getName().startsWith("accessClassInPackage.")) {
      checkPackageAccess(permission.getName().substring(21));
      return;
    } else if(permission.getName().startsWith("loadLibrary.")) {
      checkLoadLibrary(permission.getName().substring(12));
      return;
    }

    switch (permission.getName()) {
      case "accessDeclaredMembers":
      case "createClassLoader":
      case "getClassLoader":
      case "setContextClassLoader":
      case "getProtectionDomain":
      case "modifyThreadGroup":
      case "modifyThread":
        break;

      case "shutdownHooks":
        if(callStack.contains(Font.class)) {
          break;
        }

      default:
        fail(permission);
    }
  }

  private void checkPackageAccess(String pkg) {
    // JavafX
    if(
        !pkg.startsWith("com.sun.javafx") &&
        !pkg.startsWith("javafx") &&
        !pkg.startsWith("com.sun.glass") &&
        !pkg.startsWith("com.sun.scenario") &&
        !pkg.startsWith("sun.util.logging")
    ) {
      throw new AccessControlException("Theme scripts are not allowed to access classes in " + pkg + " using reflection");
    }
  }

  private void checkLoadLibrary(String path) {
    if(!path.contains("/")) {
      return;
    }

    Path targetPath = Paths.get(path).toAbsolutePath();

    String[] libraryPaths = System.getProperty("java.library.path", "").split(File.pathSeparator);
    for(String libraryPath : libraryPaths) {
      if(targetPath.startsWith(Paths.get(libraryPath).toAbsolutePath())) {
        return;
      }
    }

    throw new AccessControlException("Theme scripts are not allowed to load libraries outside of java.library.path: " + path);
  }

  private void checkFile(FilePermission permission) {
    Path targetPath = Paths.get(permission.getName());
    Path parent = targetPath.getParent();
    if(parent == null) {
      throw new AccessControlException("Theme scripts are not allowed to access files at the system root");
    }

    if(targetPath.startsWith(TEMPORARY_DIR)) {
      if(!themePaths.contains(targetPath) && Files.exists(targetPath)) {
        throw new AccessControlException(
            "Theme scripts are not allowed to access temporary files not created by them: " + targetPath.toString());
      } else {
        if(permission.getActions().equals("write")) {
          themePaths.add(targetPath);
        }
        return;
      }
    }

    if(!permission.getActions().equals("read")) {
      throw new AccessControlException("Theme scripts are not allowed to access files other than for reading");
    }

    if(permission.getName().endsWith(".ttf")) {
      return;
    }

    parent = parent.toAbsolutePath();

    String[] libraryPaths = System.getProperty("java.library.path", "").split(File.pathSeparator);
    for(String libraryPath : libraryPaths) {
      if(parent.equals(Paths.get(libraryPath).toAbsolutePath())) {
        return;
      }
    }

    String[] classPath = System.getProperty("java.class.path", "").split(File.pathSeparator);
    for(String classPathEntry : classPath) {
      if(targetPath.startsWith(Paths.get(classPathEntry).toAbsolutePath())) {
        return;
      }
    }

    if(targetPath.startsWith(JAVA_INSTALLATION_HOME)) {
      return;
    }

    throw new AccessControlException("Theme scripts are not allowed to access files outside of the native library path " +
        "or the java system home: " + targetPath.toString());
  }

  private void checkReflect(ReflectPermission permission) {
    if(!permission.getName().equals("suppressAccessChecks")) {
      fail(permission);
    }
  }

  private void checkProperty(PropertyPermission permission, List<Class<?>> callStack) {
    if(!permission.getActions().equals("read")) {
      throw new AccessControlException("Theme scripts are not allowed to access properties other than for reading");
    }

    // JavaFX
    if(
        permission.getName().startsWith("prism.") ||
        permission.getName().startsWith("javafx.") ||
        permission.getName().startsWith("com.sun.javafx")
    ) {
      return;
    }

    for(Class<?> caller : callStack) {
      if(caller.getName().equals("net.labyfy.component.packages.impl.source.FileSource")) {
        return;
      }
    }

    switch (permission.getName()) {
      // Common Java properties
      case "java.library.path":
      case "java.home":
      case "line.separator":
      case "java.io.tmpdir":
        return;

      default:
        throw new AccessControlException("Theme scripts are not allowed to access the property " + permission.getName());

    }
  }

  private void checkAll(AllPermission permission, List<Class<?>> callStack) {
    for(Class<?> caller : callStack) {
      if(caller == Font.class) {
        return;
      }
    }

    fail(permission);
  }

  private void fail(Permission permission) {
    throw new AccessControlException("Theme scripts don't have the permission " + permission.toString());
  }
}
