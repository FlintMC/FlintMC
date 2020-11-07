package net.labyfy.component.player.type.sound;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration of all available sound categories.
 */
public enum SoundCategory {

  /**
   * The master sound category.
   */
  @DisplayName(@Component(value = "soundCategory.master", translate = true))
  MASTER,

  /**
   * The music sound category.
   */
  @DisplayName(@Component(value = "soundCategory.music", translate = true))
  MUSIC,

  /**
   * The record sound category.
   */
  @DisplayName(@Component(value = "soundCategory.record", translate = true))
  RECORD,

  /**
   * The weather sound category.
   */
  @DisplayName(@Component(value = "soundCategory.weather", translate = true))
  WEATHER,

  /**
   * The block sound category.
   */
  @DisplayName(@Component(value = "soundCategory.block", translate = true))
  BLOCK,

  /**
   * The hostile sound category.
   */
  @DisplayName(@Component(value = "soundCategory.hostile", translate = true))
  HOSTILE,

  /**
   * The neutral sound category.
   */
  @DisplayName(@Component(value = "soundCategory.neutral", translate = true))
  NEUTRAL,

  /**
   * The player sound category.
   */
  @DisplayName(@Component(value = "soundCategory.player", translate = true))
  PLAYER,

  /**
   * The ambient sound category.
   */
  @DisplayName(@Component(value = "soundCategory.ambient", translate = true))
  AMBIENT,

  /**
   * The voice sound category.
   */
  @DisplayName(@Component(value = "soundCategory.voice", translate = true))
  VOICE;

  /**
   * Retrieves the name of this sound category.
   *
   * @return The name of this sound category.
   */
  public String getName() {
    return this.name().toLowerCase();
  }
}
