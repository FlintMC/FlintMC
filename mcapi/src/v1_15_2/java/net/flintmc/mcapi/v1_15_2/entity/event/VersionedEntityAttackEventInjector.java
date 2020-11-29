package net.flintmc.mcapi.v1_15_2.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityAttackEvent;
import net.flintmc.mcapi.entity.event.EntityInteractEvent;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.items.inventory.player.PlayerHand;
import net.flintmc.transform.hook.Hook;
import net.minecraft.util.Hand;

@Singleton
public class VersionedEntityAttackEventInjector {

  private final EventBus eventBus;
  private final EntityMapper entityMapper;
  private final EntityAttackEvent.Factory attackEventFactory;
  private final EntityInteractEvent.Factory interactEventFactory;

  @Inject
  private VersionedEntityAttackEventInjector(
      EventBus eventBus,
      EntityMapper entityMapper,
      EntityAttackEvent.Factory attackEventFactory,
      EntityInteractEvent.Factory interactEventFactory) {
    this.eventBus = eventBus;
    this.entityMapper = entityMapper;
    this.attackEventFactory = attackEventFactory;
    this.interactEventFactory = interactEventFactory;
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.PlayerController",
      methodName = "attackEntity",
      parameters = {
        @Type(typeName = "net.minecraft.entity.player.PlayerEntity"),
        @Type(typeName = "net.minecraft.entity.Entity")
      })
  public void attackEntity(@Named("args") Object[] args) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[1]);
    if (entity == null) {
      return;
    }

    EntityAttackEvent event = this.attackEventFactory.create(entity);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.PlayerController",
      methodName = "interactWithEntity",
      parameters = {
        @Type(typeName = "net.minecraft.entity.player.PlayerEntity"),
        @Type(typeName = "net.minecraft.entity.Entity"),
        @Type(reference = Hand.class)
      })
  public void interactWithEntity(@Named("args") Object[] args) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[1]);
    if (entity == null) {
      return;
    }

    PlayerHand hand = args[2] == Hand.MAIN_HAND ? PlayerHand.MAIN_HAND : PlayerHand.OFF_HAND;

    EntityInteractEvent event = this.interactEventFactory.create(entity, hand);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);
  }
}
