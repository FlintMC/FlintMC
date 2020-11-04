package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.gamesettings.configuration.SoundConfiguration;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.player.type.sound.SoundMapper;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of {@link SoundConfiguration}.
 */
@Singleton
@ConfigImplementation(value = SoundConfiguration.class, version = "1.15.2")
public class VersionedSoundConfiguration implements SoundConfiguration {

  private final SoundMapper soundMapper;

  @Inject
  private VersionedSoundConfiguration(SoundMapper soundMapper) {
    this.soundMapper = soundMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSoundVolume(SoundCategory soundCategory) {
    return Minecraft.getInstance().gameSettings.getSoundLevel((net.minecraft.util.SoundCategory) this.soundMapper.toMinecraftSoundCategory(soundCategory));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSoundVolume(SoundCategory soundCategory, float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(
        (net.minecraft.util.SoundCategory) this.soundMapper.toMinecraftSoundCategory(soundCategory),
        volume
    );
  }

}
