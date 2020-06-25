package net.labyfy.internal.component.security;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.security.AddPermissionCheckerPermission;
import net.labyfy.component.security.LabyfySecurityManager;
import net.labyfy.component.security.PermissionChecker;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of the security manager.
 */
@Singleton
@AutoLoad
@Implement(LabyfySecurityManager.class)
public final class DefaultSecurityManager extends SecurityManager implements LabyfySecurityManager {
  private final List<PermissionChecker> checkers;

  // Thread local to prevent concurrency issues
  private final ThreadLocal<Boolean> inPermissionCheck;

  @Inject
  private DefaultSecurityManager() {
    this.checkers = new ArrayList<>();
    this.inPermissionCheck = new ThreadLocal<>();
  }

  /**
   * Install our self as the security manager before minecraft starts
   */
  @Task(Tasks.PRE_MINECRAFT_INITIALIZE)
  public void install() {
    if(Boolean.getBoolean("labyfy.enableSecurityManager")) {
      // Due to some performance impact the security manager needs to
      // be enabled explicitly
      System.setSecurityManager(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkPermission(Permission perm) {
    if (perm.getName() != null && perm.getName().equals("setSecurityManager")) {
      throw new SecurityException("Replacing the Labyfy security manager is not allowed");
    }

    if (inPermissionCheck.get() != null && inPermissionCheck.get()) {
      // If we are currently within a permission check, grant every permission
      return;
    }

    inPermissionCheck.set(true);
    try {
      // Retrieve the callstack and pass it on to every checker
      @SuppressWarnings("unchecked")
      List<Class<?>> callStack = (List<Class<?>>) (List<?>) Arrays.asList(getClassContext());
      checkers.forEach(checker -> checker.check(callStack, perm));
    } finally {
      // Make sure we unset the flag, since the guarded code will sometimes throw
      // security exceptions on purpose
      inPermissionCheck.set(false);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installChecker(PermissionChecker checker) {
    checkPermission(new AddPermissionCheckerPermission());
    checkers.add(checker);
  }
}
