package net.flintmc.mcapi.v1_15_2.player.network;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

/** 1.15.2 implementation of the {@link NetworkPlayerInfo} */
@Implement(value = NetworkPlayerInfo.class, version = "1.15.2")
public class VersionedNetworkPlayerInfo implements NetworkPlayerInfo {

  private final GameProfile gameProfile;
  private final EntityFoundationMapper entityFoundationMapper;
  private final Scoreboard scoreboard;

  @AssistedInject
  private VersionedNetworkPlayerInfo(
      @Assisted("gameProfile") GameProfile gameProfile,
      Scoreboard scoreboard,
      EntityFoundationMapper entityFoundationMapper) {
    this.gameProfile = gameProfile;
    this.scoreboard = scoreboard;
    this.entityFoundationMapper = entityFoundationMapper;
  }

  /** {@inheritDoc} */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfile;
  }

  /** {@inheritDoc} */
  @Override
  public int getResponseTime() {
    return this.getPlayerInfo().getResponseTime();
  }

  /** {@inheritDoc} */
  @Override
  public GameMode getGameMode() {
    return this.entityFoundationMapper.fromMinecraftGameType(this.getPlayerInfo().getGameType());
  }

  /** {@inheritDoc} */
  @Override
  public PlayerTeam getPlayerTeam() {
    return this.scoreboard.getPlayerTeam(this.gameProfile.getName());
  }

  /** {@inheritDoc} */
  @Override
  public int getLastHealth() {
    return this.getPlayerInfo().getLastHealth();
  }

  /** {@inheritDoc} */
  @Override
  public int getDisplayHealth() {
    return this.getPlayerInfo().getDisplayHealth();
  }

  /** {@inheritDoc} */
  @Override
  public long getLastHealthTime() {
    return this.getPlayerInfo().getLastHealthTime();
  }

  /** {@inheritDoc} */
  @Override
  public long getHealthBlinkTime() {
    return this.getPlayerInfo().getHealthBlinkTime();
  }

  /** {@inheritDoc} */
  @Override
  public long getRenderVisibilityId() {
    return this.getPlayerInfo().getRenderVisibilityId();
  }

  @Override
  public ChatComponent getDisplayName() {
    ITextComponent displayName = this.getPlayerInfo().getDisplayName();
    if (displayName == null) {
      return null;
    }

    return this.entityFoundationMapper.getComponentMapper().fromMinecraft(displayName);
  }

  /** {@inheritDoc} */
  @Override
  public SkinModel getSkinModel() {
    return SkinModel.getModel(this.getPlayerInfo().getSkinType());
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.mapLocation(this.getPlayerInfo().getLocationSkin());
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.mapLocation(this.getPlayerInfo().getLocationCape());
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.mapLocation(this.getPlayerInfo().getLocationElytra());
  }

  private ResourceLocation mapLocation(net.minecraft.util.ResourceLocation minecraftLocation) {
    if (minecraftLocation == null) {
      return null;
    }

    return this.entityFoundationMapper
        .getResourceLocationProvider()
        .get(minecraftLocation.getNamespace(), minecraftLocation.getPath());
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasSkin() {
    return this.getSkinLocation() != null;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCloak() {
    return this.getCloakLocation() != null;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasElytra() {
    return this.getElytraLocation() != null;
  }

  /**
   * Retrieves a {@link net.minecraft.client.network.play.NetworkPlayerInfo} with the unique
   * identifier from the game profile in this class.
   *
   * @return A {@link net.minecraft.client.network.play.NetworkPlayerInfo} or {@code null}
   */
  private net.minecraft.client.network.play.NetworkPlayerInfo getPlayerInfo() {
    UUID uniqueId = this.gameProfile.getUniqueId();
    return Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId);
  }
}
