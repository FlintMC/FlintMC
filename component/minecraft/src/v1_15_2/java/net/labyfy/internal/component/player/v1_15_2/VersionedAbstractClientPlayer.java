package net.labyfy.internal.component.player.v1_15_2;

import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.player.AbstractClientPlayerEntity;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.ClientWorld;

/**
 * 1.15.2 implementation of the {@link AbstractClientPlayerEntity}.
 */
public class VersionedAbstractClientPlayer extends VersionedPlayerEntity implements AbstractClientPlayerEntity {

  private final net.minecraft.client.entity.player.AbstractClientPlayerEntity entity;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;

  public VersionedAbstractClientPlayer(
          Object entity,
          EntityType entityType,
          ClientWorld world,
          EntityMapper entityMapper,
          GameProfileSerializer gameProfileGameProfileSerializer,
          ModelMapper modelMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry) {
    super(entity, entityType, world, entityMapper, gameProfileGameProfileSerializer, modelMapper);
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;

    if (!(entity instanceof net.minecraft.client.entity.player.AbstractClientPlayerEntity)) {
      throw new IllegalArgumentException("");
    }

    this.entity = (net.minecraft.client.entity.player.AbstractClientPlayerEntity) entity;
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
  public boolean hasPlayerInfo() {
    return this.entity.hasPlayerInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(entity.getGameProfile().getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return this.entity.getFovModifier();
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
}
