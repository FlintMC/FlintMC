package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.tileentity.mapper.TileEntityMapper;
import net.labyfy.component.world.World;

/**
 * 1.15.2 implementation of the {@link RemoteClientPlayer}.
 */
@Implement(value = RemoteClientPlayer.class, version = "1.15.2")
public class VersionedRemoteClientPlayer extends VersionedPlayerEntity implements RemoteClientPlayer {

  private final net.minecraft.client.entity.player.RemoteClientPlayerEntity playerEntity;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;

  @AssistedInject
  private VersionedRemoteClientPlayer(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper,
          GameProfileSerializer gameProfileSerializer,
          ModelMapper modelMapper,
          NBTMapper nbtMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          TileEntityMapper tileEntityMapper) {
    super(entity, entityType, world, entityBaseMapper, gameProfileSerializer, modelMapper, nbtMapper, tileEntityMapper);

    if (!(entity instanceof net.minecraft.client.entity.player.RemoteClientPlayerEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.client.entity.player.RemoteClientPlayerEntity.class.getName());
    }
    this.playerEntity = (net.minecraft.client.entity.player.RemoteClientPlayerEntity) entity;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(playerEntity.getGameProfile().getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return this.playerEntity.getFovModifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild() {
    return this.playerEntity.isChild();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraPitch() {
    return this.playerEntity.rotateElytraX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraPitch(float elytraPitch) {
    this.playerEntity.rotateElytraX = elytraPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraYaw() {
    return this.playerEntity.rotateElytraY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraYaw(float elytraYaw) {
    this.playerEntity.rotateElytraY = elytraYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraRoll() {
    return this.playerEntity.rotateElytraZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraRoll(float elytraRoll) {
    this.playerEntity.rotateElytraZ = elytraRoll;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpectator() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.SPECTATOR;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreative() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.CREATIVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkDespawn() {
    this.playerEntity.checkDespawn();
  }
}
