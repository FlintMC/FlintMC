package net.flintmc.mcapi.v1_15_2.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.player.type.sound.SoundMapper;
import net.flintmc.mcapi.world.event.SoundPlayEvent;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedSoundPlayEventInjector {

  private final EventBus eventBus;
  private final SoundMapper soundMapper;
  private final Vector3D.Factory vectorFactory;
  private final SoundPlayEvent.Factory soundEventFactory;

  @Inject
  private VersionedSoundPlayEventInjector(
      EventBus eventBus,
      SoundMapper soundMapper,
      Vector3D.Factory vectorFactory,
      SoundPlayEvent.Factory soundEventFactory) {
    this.eventBus = eventBus;
    this.soundMapper = soundMapper;
    this.vectorFactory = vectorFactory;
    this.soundEventFactory = soundEventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "playSound",
      parameters = {
        @Type(reference = double.class), // x
        @Type(reference = double.class), // y
        @Type(reference = double.class), // z
        @Type(typeName = "net.minecraft.util.SoundEvent"),
        @Type(typeName = "net.minecraft.util.SoundCategory"),
        @Type(reference = float.class), // volume
        @Type(reference = float.class), // pitch
        @Type(reference = boolean.class) // distanceDelay
      },
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER})
  public void playSound(@Named("args") Object[] args, ExecutionTime executionTime) {
    Vector3D position =
        this.vectorFactory.create((double) args[0], (double) args[1], (double) args[2]);
    Sound sound = this.soundMapper.fromMinecraftSoundEvent(args[3]);
    SoundCategory category = this.soundMapper.fromMinecraftSoundCategory(args[4]);
    float volume = (float) args[5];
    float pitch = (float) args[6];

    SoundPlayEvent event = this.soundEventFactory.create(position, sound, category, volume, pitch);
    this.eventBus.fireEvent(event, executionTime);
  }
}
