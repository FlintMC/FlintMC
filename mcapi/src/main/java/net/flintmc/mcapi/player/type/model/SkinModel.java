package net.flintmc.mcapi.player.type.model;

import java.util.HashMap;
import java.util.Map;

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

  private static final Map<String, SkinModel> BY_NAME = new HashMap<>();

  static {
    for (SkinModel value : values()) {
      BY_NAME.put(value.model, value);
    }
  }

  private final String model;

  /**
   * Default constructor
   *
   * @param model The official mojang name of this skin model
   */
  SkinModel(String model) {
    this.model = model;
  }

  /**
   * Retrieves the model by the given name.
   *
   * @param name The name of the model.
   * @return A model by the given name or {@link #STEVE}.
   */
  public static SkinModel getModel(String name) {
    return BY_NAME.getOrDefault(name, STEVE);
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
