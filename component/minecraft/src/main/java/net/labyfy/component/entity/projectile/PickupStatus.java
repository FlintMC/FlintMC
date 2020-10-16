package net.labyfy.component.entity.projectile;

/**
 * An enumeration for all pickup statuses.
 */
public enum PickupStatus {

  DISALLOWED(0),
  ALLOWED(1),
  CREATIVE_ONLY(2),
  ;

  private final int identifier;

  PickupStatus(int identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the identifier of the pickup status.
   *
   * @return The pickup status identifier.
   */
  public int getIdentifier() {
    return identifier;
  }
}
