package net.labyfy.component.gamesettings.configuration;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;

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

  // TODO for enums in multi getters, there should be a setting which generates one button per enum constant (e.g. for the key bindings and the sound categories, the data should then be provided on the enum constant)

  /**
   * Retrieves an sound volume of the given sound category.
   *
   * @param soundCategory The sound category to get the volume.
   * @return The sound volume of the sound category.
   */
  @SliderSetting(@Range(max = 1, decimals = 2))
  float getSoundVolume(SoundCategory soundCategory);

  /**
   * Changes the volume of the given sound category.
   *
   * @param soundCategory The sound category to be changed
   * @param volume        The new sound volume for the category.
   */
  void setSoundVolume(SoundCategory soundCategory, float volume);

}
