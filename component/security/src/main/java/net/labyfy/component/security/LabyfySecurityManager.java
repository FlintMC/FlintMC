package net.labyfy.component.security;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

import java.security.Permission;
import java.util.*;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
public class LabyfySecurityManager extends SecurityManager {
  private final List<PermissionChecker> checkers;
  private final ThreadLocal<Boolean> inPermissionCheck;

  @Inject
  private LabyfySecurityManager() {
    this.checkers = new ArrayList<>();
    this.inPermissionCheck = new ThreadLocal<>();
  }

  @TaskBody
  public void install() {
    System.setSecurityManager(this);
  }

  @Override
  public void checkPermission(Permission perm) {
    if (perm.getName() != null && perm.getName().equals("setSecurityManager")) {
      throw new SecurityException("Replacing the Labyfy security manager is not allowed");
    }

    if (inPermissionCheck.get() != null && inPermissionCheck.get()) {
      return;
    }

    inPermissionCheck.set(true);
    try {
      @SuppressWarnings("unchecked")
      List<Class<?>> callStack = (List<Class<?>>) (List<?>) Arrays.asList(getClassContext());
      checkers.forEach(checker -> checker.check(callStack, perm));
    } finally {
      inPermissionCheck.set(false);
    }
  }

  public void installChecker(PermissionChecker checker) {
    checkPermission(new AddPermissionCheckerPermission());
    checkers.add(checker);
  }
}
