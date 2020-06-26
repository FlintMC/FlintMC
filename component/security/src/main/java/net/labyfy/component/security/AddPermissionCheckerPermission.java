package net.labyfy.component.security;

import java.security.Permission;

/**
 * Represents the permission required for {@link LabyfySecurityManager#installChecker(PermissionChecker)}.
 */
public final class AddPermissionCheckerPermission extends Permission {
  public AddPermissionCheckerPermission() {
    super("addPermissionChecker");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean implies(Permission permission) {
    return permission instanceof AddPermissionCheckerPermission;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof AddPermissionCheckerPermission)) {
      return false;
    }

    return ((AddPermissionCheckerPermission) obj).getActions().equals("addPermissionChecker");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return getClass().hashCode() ^ "addPermissionChecker".hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getActions() {
    return "addPermissionChecker";
  }
}
