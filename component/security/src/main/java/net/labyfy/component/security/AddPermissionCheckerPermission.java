package net.labyfy.component.security;

import java.security.Permission;

public class AddPermissionCheckerPermission extends Permission {
  public AddPermissionCheckerPermission() {
    super("addPermissionChecker");
  }

  @Override
  public boolean implies(Permission permission) {
    return permission instanceof AddPermissionCheckerPermission;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String getActions() {
    return "addPermissionChecker";
  }
}
