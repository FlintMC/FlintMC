package net.flintmc.mcapi.v1_15_2.potion;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Map;
import java.util.Optional;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityDespawnEvent;
import net.flintmc.mcapi.entity.event.EntitySpawnEvent;
import net.flintmc.mcapi.potion.event.PotionAddEvent;
import net.flintmc.mcapi.potion.event.PotionRemoveEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent.State;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent.Factory;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.mcapi.world.ClientWorld;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

@Singleton
public class VersionedLivingEntityPotionInterceptor {

  private static final String LIVING_ENTITY = "net.minecraft.entity.LivingEntity";
  private final Map<Integer, PotionAddEvent> potionAddEvents;
  private final Map<Integer, PotionRemoveEvent> potionRemoveEvents;
  private final Map<Integer, PotionUpdateEvent> potionUpdateEvents;

  private final ClientWorld clientWorld;
  private final PotionMapper potionMapper;
  private final EventBus eventBus;
  private final PotionUpdateEvent.Factory potionUpdateEventFactory;
  private final PotionAddEvent.Factory potionAddEventFactory;
  private final PotionRemoveEvent.Factory potionRemoveEventFactory;
  private final PotionStateUpdateEvent.Factory potionStateUpdateEventFactory;

  @Inject
  public VersionedLivingEntityPotionInterceptor(
      ClientWorld clientWorld,
      PotionMapper potionMapper,
      EventBus eventBus,
      Factory potionUpdateEventFactory,
      PotionAddEvent.Factory potionAddEventFactory,
      PotionRemoveEvent.Factory potionRemoveEventFactory,
      PotionStateUpdateEvent.Factory potionStateUpdateEventFactory) {
    this.potionAddEvents = Maps.newHashMap();
    this.potionRemoveEvents = Maps.newHashMap();
    this.potionUpdateEvents = Maps.newHashMap();
    this.clientWorld = clientWorld;
    this.potionMapper = potionMapper;
    this.eventBus = eventBus;
    this.potionAddEventFactory = potionAddEventFactory;
    this.potionRemoveEventFactory = potionRemoveEventFactory;
    this.potionUpdateEventFactory = potionUpdateEventFactory;
    this.potionStateUpdateEventFactory = potionStateUpdateEventFactory;
  }

  @PreSubscribe
  public void addEntity(EntitySpawnEvent event) {
    if (event.getEntity() instanceof net.flintmc.mcapi.entity.LivingEntity) {
      this.potionUpdateEvents.put(event.getIdentifier(), null);
    }
  }

  @PreSubscribe
  public void removeEntity(EntityDespawnEvent event) {
    if (event.getEntity() instanceof net.flintmc.mcapi.entity.LivingEntity) {
      this.potionUpdateEvents.remove(event.getEntity().getIdentifier());
    }
  }

  @Hook(
      executionTime = {ExecutionTime.AFTER, ExecutionTime.BEFORE},
      className = LIVING_ENTITY,
      methodName = "updatePotionEffects")
  public void hookUpdatePotionEffects(
      @Named("instance") Object owner, ExecutionTime executionTime) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              if (this.potionUpdateEvents.get(livingEntity.getIdentifier()) == null) {
                PotionUpdateEvent potionUpdateEvent =
                    this.potionUpdateEventFactory.create(livingEntity);
                this.potionUpdateEvents.put(livingEntity.getIdentifier(), potionUpdateEvent);
                this.eventBus.fireEvent(potionUpdateEvent, executionTime);
              } else {
                this.eventBus.fireEvent(
                    this.potionUpdateEvents.get(livingEntity.getIdentifier()), executionTime);
              }
            });
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "addPotionEffect",
      parameters = {@Type(reference = EffectInstance.class)})
  public void hookAddPotionEffect(@Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              if (this.potionAddEvents.get(livingEntity.getIdentifier()) == null) {
                PotionAddEvent potionAddEvent =
                    this.potionAddEventFactory.create(
                        livingEntity, this.potionMapper.fromMinecraftEffectInstance(args[0]));
                this.potionAddEvents.put(livingEntity.getIdentifier(), potionAddEvent);
                this.eventBus.fireEvent(potionAddEvent, Phase.PRE);
              } else {
                this.eventBus.fireEvent(
                    this.potionAddEvents.get(livingEntity.getIdentifier()), Phase.PRE);
              }
            });
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "removeActivePotionEffect",
      parameters = {@Type(reference = Effect.class)})
  public void hookRemoveActivePotionEffect(
      @Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              if (this.potionRemoveEvents.get(livingEntity.getIdentifier()) == null) {
                PotionRemoveEvent potionRemoveEvent =
                    this.potionRemoveEventFactory.create(
                        livingEntity, this.potionMapper.fromMinecraftEffect(args[0]));
                this.potionRemoveEvents.put(livingEntity.getIdentifier(), potionRemoveEvent);
                this.eventBus.fireEvent(potionRemoveEvent, Phase.PRE);
              } else {
                this.eventBus.fireEvent(
                    this.potionRemoveEvents.get(livingEntity.getIdentifier()), Phase.PRE);
              }
            });
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onNewPotionEffect",
      parameters = {@Type(reference = EffectInstance.class)})
  public void hookOnNewPotionEffect(@Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity ->
                this.eventBus.fireEvent(
                    this.potionStateUpdateEventFactory.create(
                        livingEntity,
                        this.potionMapper.fromMinecraftEffectInstance(args[0]),
                        State.NEW),
                    Phase.PRE));
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onChangedPotionEffect",
      parameters = {@Type(reference = EffectInstance.class), @Type(reference = boolean.class)})
  public void hookOnChangedPotionEffect(
      @Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity ->
                this.eventBus.fireEvent(
                    this.potionStateUpdateEventFactory.create(
                        livingEntity,
                        this.potionMapper.fromMinecraftEffectInstance(args[0]),
                        State.CHANGED),
                    Phase.PRE));
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onFinishedPotionEffect",
      parameters = {@Type(reference = EffectInstance.class)})
  public void hookOnFinishedPotionEffect(
      @Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              this.eventBus.fireEvent(
                  this.potionStateUpdateEventFactory.create(
                      livingEntity,
                      this.potionMapper.fromMinecraftEffectInstance(args[0]),
                      State.FINISHED),
                  Phase.PRE);
            });
  }

  private Optional<net.flintmc.mcapi.entity.LivingEntity> getLivingEntity(
      LivingEntity livingEntity) {
    Optional<net.flintmc.mcapi.entity.LivingEntity> optionalLivingEntity = Optional.empty();

    Entity entity = this.clientWorld.getEntityByIdentifier(livingEntity.getEntityId());
    if (entity == null) {
      return optionalLivingEntity;
    }

    if (entity instanceof net.flintmc.mcapi.entity.LivingEntity) {
      net.flintmc.mcapi.entity.LivingEntity flintLivingEntity =
          (net.flintmc.mcapi.entity.LivingEntity) entity;
      optionalLivingEntity = Optional.of(flintLivingEntity);
    }

    return optionalLivingEntity;
  }
}
