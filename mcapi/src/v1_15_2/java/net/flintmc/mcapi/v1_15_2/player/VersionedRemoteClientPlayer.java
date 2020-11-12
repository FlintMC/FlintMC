package net.flintmc.mcapi.v1_15_2.player;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.player.RemoteClientPlayer;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.world.World;
import net.flintmc.render.model.ModelBox;

/** 1.15.2 implementation of the {@link RemoteClientPlayer}. */
@Implement(value = RemoteClientPlayer.class, version = "1.15.2")
public class VersionedRemoteClientPlayer extends VersionedPlayerEntity
    implements RemoteClientPlayer {

  private final net.minecraft.client.entity.player.RemoteClientPlayerEntity playerEntity;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;

  @AssistedInject
  private VersionedRemoteClientPlayer(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      GameProfileSerializer gameProfileSerializer,
      ModelMapper modelMapper,
      NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
      ItemEntityMapper itemEntityMapper,
      TileEntityMapper tileEntityMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(
        entity,
        entityType,
        world,
        entityFoundationMapper,
        gameProfileSerializer,
        modelMapper,
        itemEntityMapper,
        tileEntityMapper,
        entityRenderContextFactory
    );

    if (!(entity instanceof net.minecraft.client.entity.player.RemoteClientPlayerEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.client.entity.player.RemoteClientPlayerEntity.class.getName());
    }
    this.playerEntity = (net.minecraft.client.entity.player.RemoteClientPlayerEntity) entity;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /** {@inheritDoc} */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(playerEntity.getGameProfile().getId());
  }

  /** {@inheritDoc} */
  @Override
  public float getFovModifier() {
    return this.playerEntity.getFovModifier();
  }

  /** {@inheritDoc} */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isChild() {
    return this.playerEntity.isChild();
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraPitch() {
    return this.playerEntity.rotateElytraX;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraPitch(float elytraPitch) {
    this.playerEntity.rotateElytraX = elytraPitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraYaw() {
    return this.playerEntity.rotateElytraY;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraYaw(float elytraYaw) {
    this.playerEntity.rotateElytraY = elytraYaw;
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraRoll() {
    return this.playerEntity.rotateElytraZ;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraRoll(float elytraRoll) {
    this.playerEntity.rotateElytraZ = elytraRoll;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpectator() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.SPECTATOR;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCreative() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.CREATIVE;
  }

  /** {@inheritDoc} */
  @Override
  public void checkDespawn() {
    this.playerEntity.checkDespawn();
  }
}
