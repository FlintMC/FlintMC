package net.labyfy.internal.component.player.v1_15_2;

import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.player.AbstractClientPlayerEntity;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.ClientWorld;

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

  @Override
  public boolean hasPlayerInfo() {
    return this.entity.hasPlayerInfo();
  }

  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(entity.getGameProfile().getId());
  }

  @Override
  public float getFovModifier() {
    return this.entity.getFovModifier();
  }

  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }
}
