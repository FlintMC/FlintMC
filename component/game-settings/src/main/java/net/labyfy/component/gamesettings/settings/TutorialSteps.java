package net.labyfy.component.gamesettings.settings;

public enum TutorialSteps {

  MOVEMENT("movement"),
  FIND_TREE("find_tree"),
  PUNCH_TREE("punch_tree"),
  OPEN_INVENTORY("open_inventory"),
  CRAFT_PLANKS("craft_planks"),
  NONE("none");

  private final String name;

  TutorialSteps(String nameIn) {
    this.name = nameIn;
  }

  public String getName() {
    return this.name;
  }
}