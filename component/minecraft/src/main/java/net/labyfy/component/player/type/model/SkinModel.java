package net.labyfy.component.player.type.model;

/**
 * An enumeration that shows all available skin models for a player.
 */
public enum SkinModel {

  /**
   * The skin model with 4-pixel large arms
   */
  STEVE("default"),
  /**
   * The skin model with 3-pixel large arms
   */
  ALEX("slim");

  private final String model;

  /**
   * Default constructor
   *
   * @param model The official mojang name of this skin model
   */
  SkinModel(String model) {
    this.model = model;
  }

  public static SkinModel getModel(String name) {
    for (SkinModel value : values()) {
      if (value.getModel().equalsIgnoreCase(name)) {
        return value;
      }
    }
    return STEVE;
  }

  /**
   * Retrieves the official mojang name of this skin model
   *
   * @return The official mojang name of this skin model
   */
  public String getModel() {
    return model;
  }

}
