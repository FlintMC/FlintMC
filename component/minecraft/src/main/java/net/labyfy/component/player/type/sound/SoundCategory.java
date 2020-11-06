package net.labyfy.component.player.type.sound;

/**
 * An enumeration of all available sound categories.
 */
public enum SoundCategory {

  /**
   * The master sound category.
   */
  MASTER,
  /**
   * The music sound category.
   */
  MUSIC,
  /**
   * The record sound category.
   */
  RECORD,
  /**
   * The weather sound category.
   */
  WEATHER,
  /**
   * The block sound category.
   */
  BLOCK,
  /**
   * The hostile sound category.
   */
  HOSTILE,
  /**
   * The neutral sound category.
   */
  NEUTRAL,
  /**
   * The player sound category.
   */
  PLAYER,
  /**
   * The ambient sound category.
   */
  AMBIENT,
  /**
   * The voice sound category.
   */
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
