package net.flintmc.mcapi.internal.world.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.world.event.SoundPlayEvent;
import net.flintmc.mcapi.world.math.Vector3D;

/** {@inheritDoc} */
@Implement(SoundPlayEvent.class)
public class DefaultSoundPlayEvent implements SoundPlayEvent {

  private final Vector3D position;
  private final Sound sound;
  private final SoundCategory category;
  private final float volume;
  private final float pitch;

  @AssistedInject
  public DefaultSoundPlayEvent(
      @Assisted Vector3D position,
      @Assisted Sound sound,
      @Assisted SoundCategory category,
      @Assisted("volume") float volume,
      @Assisted("pitch") float pitch) {
    this.position = position;
    this.sound = sound;
    this.category = category;

    this.volume = volume;
    this.pitch = pitch;
  }

  /** {@inheritDoc} */
  @Override
  public Vector3D getPosition() {
    return this.position;
  }

  /** {@inheritDoc} */
  @Override
  public Sound getSound() {
    return this.sound;
  }

  /** {@inheritDoc} */
  @Override
  public SoundCategory getCategory() {
    return this.category;
  }

  /** {@inheritDoc} */
  @Override
  public float getVolume() {
    return this.volume;
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch() {
    return this.pitch;
  }
}
