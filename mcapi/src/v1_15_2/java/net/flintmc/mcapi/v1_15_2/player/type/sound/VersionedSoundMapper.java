package net.flintmc.mcapi.v1_15_2.player.type.sound;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.player.type.sound.SoundMapper;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

/** 1.15.2 implementation of the {@link SoundMapper}. */
@Singleton
@Implement(value = SoundMapper.class, version = "1.15.2")
public class VersionedSoundMapper implements SoundMapper {

  private final Sound.Factory soundFactory;

  @Inject
  private VersionedSoundMapper(Sound.Factory soundFactory) {
    this.soundFactory = soundFactory;
  }

  /** {@inheritDoc} */
  @Override
  public Sound fromMinecraftSoundEvent(Object soundEvent) {
    if (!(soundEvent instanceof SoundEvent)) {
      throw new IllegalArgumentException("");
    }

    SoundEvent minecraftSoundEvent = (SoundEvent) soundEvent;

    return this.soundFactory.create(minecraftSoundEvent.getName().getPath());
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftSoundEvent(Sound sound) {
    Optional<SoundEvent> optional = Registry.SOUND_EVENT.getValue(sound.getName().getHandle());
    return optional.orElseGet(() -> new SoundEvent(sound.getName().getHandle()));
  }

  /** {@inheritDoc} */
  @Override
  public SoundCategory fromMinecraftSoundCategory(Object soundCategory) {
    if (!(soundCategory instanceof net.minecraft.util.SoundCategory)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.util.SoundCategory minecraftSoundCategory =
        (net.minecraft.util.SoundCategory) soundCategory;

    switch (minecraftSoundCategory) {
      case MUSIC:
        return SoundCategory.MUSIC;
      case RECORDS:
        return SoundCategory.RECORD;
      case WEATHER:
        return SoundCategory.WEATHER;
      case BLOCKS:
        return SoundCategory.BLOCK;
      case HOSTILE:
        return SoundCategory.HOSTILE;
      case NEUTRAL:
        return SoundCategory.NEUTRAL;
      case PLAYERS:
        return SoundCategory.PLAYER;
      case AMBIENT:
        return SoundCategory.AMBIENT;
      case VOICE:
        return SoundCategory.VOICE;
      default:
        return SoundCategory.MASTER;
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object toMinecraftSoundCategory(SoundCategory soundCategory) {
    switch (soundCategory) {
      case MUSIC:
        return net.minecraft.util.SoundCategory.MUSIC;
      case RECORD:
        return net.minecraft.util.SoundCategory.RECORDS;
      case WEATHER:
        return net.minecraft.util.SoundCategory.WEATHER;
      case BLOCK:
        return net.minecraft.util.SoundCategory.BLOCKS;
      case HOSTILE:
        return net.minecraft.util.SoundCategory.HOSTILE;
      case NEUTRAL:
        return net.minecraft.util.SoundCategory.NEUTRAL;
      case PLAYER:
        return net.minecraft.util.SoundCategory.PLAYERS;
      case AMBIENT:
        return net.minecraft.util.SoundCategory.AMBIENT;
      case VOICE:
        return net.minecraft.util.SoundCategory.VOICE;
      default:
        return net.minecraft.util.SoundCategory.MASTER;
    }
  }
}
