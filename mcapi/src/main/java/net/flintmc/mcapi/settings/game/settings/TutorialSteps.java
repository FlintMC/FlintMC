package net.flintmc.mcapi.settings.game.settings;

/**
 * An enumeration representing tutorial steps.
 */
public enum TutorialSteps {
  MOVEMENT("movement"),
  FIND_TREE("find_tree"),
  PUNCH_TREE("punch_tree"),
  OPEN_INVENTORY("open_inventory"),
  CRAFT_PLANKS("craft_planks"),
  NONE("none");

  private final String name;

  TutorialSteps(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
