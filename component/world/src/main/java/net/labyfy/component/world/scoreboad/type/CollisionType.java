package net.labyfy.component.world.scoreboad.type;


public enum CollisionType {

  ALWAYS("always", 0),
  NEVER("never", 1),
  PUSH_OTHER_TEAMS("pushOtherTeams", 2),
  PUSH_OWN_TEAM("pushOwnTeam", 3);

  private final String internalName;
  private final int identifier;

  CollisionType(String internalName, int identifier) {
    this.internalName = internalName;
    this.identifier = identifier;
  }

  /**
   * Retrieves the identifier of this collision type.
   *
   * @return The identifier of this collision type.
   */
  public int getIdentifier() {
    return identifier;
  }

  /**
   * Retrieves the translation key of this collision type.
   *
   * @return The translation key of this collision type.
   */
  public String getTranslationKey() {
    return "team.collision." + this.internalName;
  }
}
