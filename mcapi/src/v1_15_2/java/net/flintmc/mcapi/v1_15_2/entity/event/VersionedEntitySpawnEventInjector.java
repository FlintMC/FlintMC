package net.flintmc.mcapi.v1_15_2.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityDespawnEvent;
import net.flintmc.mcapi.entity.event.EntitySpawnEvent;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedEntitySpawnEventInjector {

  private final EntityMapper entityMapper;
  private final EventBus eventBus;

  private final EntitySpawnEvent.Factory spawnEventFactory;
  private final EntityDespawnEvent.Factory despawnEventFactory;

  @Inject
  private VersionedEntitySpawnEventInjector(
      EntityMapper entityMapper,
      EventBus eventBus,
      EntitySpawnEvent.Factory spawnEventFactory,
      EntityDespawnEvent.Factory despawnEventFactory) {
    this.entityMapper = entityMapper;
    this.eventBus = eventBus;
    this.spawnEventFactory = spawnEventFactory;
    this.despawnEventFactory = despawnEventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "addEntityImpl",
      parameters = {@Type(reference = int.class), @Type(typeName = "net.minecraft.entity.Entity")},
      executionTime = ExecutionTime.BEFORE)
  public void addEntity(@Named("args") Object[] args) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[1]);
    if (entity == null) {
      return;
    }

    EntitySpawnEvent event = this.spawnEventFactory.create(entity);
    this.eventBus.fireEvent(event, Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.entity.Entity",
      methodName = "remove",
      executionTime = ExecutionTime.BEFORE)
  public void remove(@Named("instance") Object instance) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(instance);
    if (entity == null) {
      return;
    }

    EntityDespawnEvent event = this.despawnEventFactory.create(entity);
    this.eventBus.fireEvent(event, Phase.PRE);
  }
}
