package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.configuration.SoundConfiguration;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of {@link SoundConfiguration}.
 */
@Singleton
@Implement(value = SoundConfiguration.class, version = "1.15.2")
public class VersionedSoundConfiguration implements SoundConfiguration {

  private final SoundCategorySerializer<net.minecraft.util.SoundCategory> soundCategorySoundSerializer;

  @Inject
  private VersionedSoundConfiguration(SoundCategorySerializer soundCategorySoundSerializer) {
    this.soundCategorySoundSerializer = soundCategorySoundSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSoundVolume(SoundCategory soundCategory) {
    return Minecraft.getInstance().gameSettings.getSoundLevel(this.soundCategorySoundSerializer.serialize(soundCategory));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSoundVolume(SoundCategory soundCategory, float volume) {
    Minecraft.getInstance().gameSettings.setSoundLevel(
            this.soundCategorySoundSerializer.serialize(soundCategory),
            volume
    );
  }
}
