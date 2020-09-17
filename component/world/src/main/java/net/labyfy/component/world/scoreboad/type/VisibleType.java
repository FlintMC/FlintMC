package net.labyfy.component.world.scoreboad.type;

/**
 *
 */
public enum VisibleType {

  ALWAYS("always", 0),
  NEVER("never", 1),
  HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
  HIDE_FOR_OWN_TEAMS("hideForOwnTeam", 3)
  ;

  private final String internalName;
  private final int id;

  VisibleType(String internalName, int id) {
    this.internalName = internalName;
    this.id = id;
  }

  /**
   * Retrieves the identifier of this visible type.
   *
   * @return The identifier of this visible type.
   */
  public int getId() {
    return id;
  }

  /**
   * Retrieves the translation key of this visible type.
   *
   * @return The translation key of this visible type.
   */
  public String getTranslationKey() {
    return "team.visibility." + this.internalName;
  }

}
