package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.settings.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.options.numeric.NumericRestriction;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;

import java.util.Map;

/**
 * Represents the sound configuration.
 */
@DefineCategory(
    name = "minecraft.settings.sounds",
    displayName = @Component(value = "minecraft.settings.sounds.display", translate = true),
    description = @Component(value = "minecraft.settings.sounds.description", translate = true)
)
@ImplementedConfig
public interface SoundConfiguration {

  /**
   * Retrieves an sound volume of the given sound category.
   *
   * @param soundCategory The sound category to get the volume.
   * @return The sound volume of the sound category.
   */
  @SliderSetting(@Range(max = 100, value = NumericRestriction.INTEGER_ONLY))
  // TODO This shouldn't work with multi getters
  float getSoundVolume(SoundCategory soundCategory);

  /**
   * Changes the volume of the given sound category.
   *
   * @param soundCategory The sound category to be changed
   * @param volume        The new sound volume for the category.
   */
  void setSoundVolume(SoundCategory soundCategory, float volume);

  // TODO maybe auto-generate this for enum keys, since the value is an enum, it should not be too hard
  Map<SoundCategory, Float> getAllSoundVolume(); // TODO rename to getAllSoundVolumes?

  void setAllSoundVolume(Map<SoundCategory, Float> volumes);

}
