package net.labyfy.component.world.scoreboad.type;

/**
 * An enumeration of all available visibility types.
 */
public enum VisibleType {

  /**
   * Visible to all players.
   */
  ALWAYS("always", 0),
  /**
   * Invisible to all players.
   */
  NEVER("never", 1),
  /**
   * The members of the own team can see it, other teams cannot.
   */
  HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
  /**
   * The members of the own team cannot see it, but other teams can see it.
   */
  HIDE_FOR_OWN_TEAMS("hideForOwnTeam", 3);

  private final String internalName;
  private final int id;

  VisibleType(String internalName, int id) {
    this.internalName = internalName;
    this.id = id;
  }

  /**
   * Retrieves the internal name of this visible type.
   *
   * @return The internal name of this visible type.
   */
  public String getInternalName() {
    return internalName;
  }

  /**
   * Retrieves the identifier of this visible type.
   *
   * @return The identifier of this visible type.
   */
  public int getId() {
    return id;
  }

}
