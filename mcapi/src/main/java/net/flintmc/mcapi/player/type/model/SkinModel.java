/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
