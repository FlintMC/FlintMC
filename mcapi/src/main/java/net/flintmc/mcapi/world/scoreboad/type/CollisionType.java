package net.flintmc.mcapi.world.scoreboad.type;

/** An enumeration of all available collision rules. */
public enum CollisionType {

  /** The members of the team can push all objects and can be pushed by all objects. */
  ALWAYS("always", 0),
  /** The members of the team cannot push an object, but they cannot be pushed either. */
  NEVER("never", 1),
  /** The members of the team can push objects of other teams, but team mates cannot. */
  PUSH_OTHER_TEAMS("pushOtherTeams", 2),
  /** The members of the team can only move objects of the same team. */
  PUSH_OWN_TEAM("pushOwnTeam", 3);

  private final String internalName;
  private final int identifier;

  CollisionType(String internalName, int identifier) {
    this.internalName = internalName;
    this.identifier = identifier;
  }

  /**
   * Retrieves the internal name of this collision type.
   *
   * @return The internal name of this collision type.
   */
  public String getInternalName() {
    return internalName;
  }

  /**
   * Retrieves the identifier of this collision type.
   *
   * @return The identifier of this collision type.
   */
  public int getIdentifier() {
    return identifier;
  }
}
