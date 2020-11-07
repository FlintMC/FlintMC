package net.flintmc.mcapi.v1_15_2.gamesettings.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.configuration.SoundConfiguration;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.player.type.sound.SoundMapper;
import net.minecraft.client.Minecraft;

/** 1.15.2 implementation of {@link SoundConfiguration}. */
@Singleton
@Implement(value = SoundConfiguration.class, version = "1.15.2")
public class VersionedSoundConfiguration implements SoundConfiguration {

  private final SoundMapper soundMapper;

  @Inject
  private VersionedSoundConfiguration(SoundMapper soundMapper) {
    this.soundMapper = soundMapper;
  }

  /** {@inheritDoc} */
  @Override
  public float getSoundVolume(SoundCategory soundCategory) {
    return Minecraft.getInstance()
        .gameSettings
        .getSoundLevel(
            (net.minecraft.util.SoundCategory)
                this.soundMapper.toMinecraftSoundCategory(soundCategory));
  }

  /** {@inheritDoc} */
  @Override
  public void setSoundVolume(SoundCategory soundCategory, float volume) {
    Minecraft.getInstance()
        .gameSettings
        .setSoundLevel(
            (net.minecraft.util.SoundCategory)
                this.soundMapper.toMinecraftSoundCategory(soundCategory),
            volume);
  }
}
