package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.config.annotation.Implemented;

/**
 * Represents the sound configuration.
 */
@Implemented
public interface SoundConfiguration {

  /**
   * Retrieves an sound volume of the given sound category.
   *
   * @param soundCategory The sound category to get the volume.
   * @return The sound volume of the sound category.
   */
  float getSoundVolume(SoundCategory soundCategory);

  /**
   * Changes the volume of the given sound category.
   *
   * @param soundCategory The sound category to be changed
   * @param volume        The new sound volume for the category.
   */
  void setSoundVolume(SoundCategory soundCategory, float volume);


}
