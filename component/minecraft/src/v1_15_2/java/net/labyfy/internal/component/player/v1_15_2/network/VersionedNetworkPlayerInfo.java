package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.minecraft.client.Minecraft;

import java.util.UUID;

/**
 * 1.15.2 implementation of the {@link NetworkPlayerInfo}
 */
@Implement(value = NetworkPlayerInfo.class, version = "1.15.2")
public class VersionedNetworkPlayerInfo implements NetworkPlayerInfo {

  private final GameProfile gameProfile;
  private final EntityBaseMapper entityBaseMapper;
  private final Scoreboard scoreboard;

  @AssistedInject
  private VersionedNetworkPlayerInfo(
          @Assisted("gameProfile") GameProfile gameProfile,
          @Assisted("scoreboard") Scoreboard scoreboard,
          @Assisted("entityMapper") EntityBaseMapper entityBaseMapper
  ) {
    this.gameProfile = gameProfile;
    this.scoreboard = scoreboard;
    this.entityBaseMapper = entityBaseMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getResponseTime() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getResponseTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode getGameMode() {
    return this.entityBaseMapper.fromMinecraftGameType(this.getPlayerInfo(this.gameProfile.getUniqueId()).getGameType());
  }

  @Override
  public PlayerTeam getPlayerTeam() {
    return this.scoreboard.getPlayerTeam(this.gameProfile.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLastHealth() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getLastHealth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getDisplayHealth() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getDisplayHealth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLastHealthTime() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getLastHealthTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getHealthBlinkTime() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getHealthBlinkTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getRenderVisibilityId() {
    return this.getPlayerInfo(this.gameProfile.getUniqueId()).getRenderVisibilityId();
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.entityBaseMapper.getComponentMapper().fromMinecraft(
            this.getPlayerInfo(this.gameProfile.getUniqueId()).getDisplayName()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return SkinModel.getModel(this.getPlayerInfo(this.gameProfile.getUniqueId()).getSkinType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.entityBaseMapper.getResourceLocationProvider().get(
            this.getPlayerInfo(this.gameProfile.getUniqueId()).getLocationSkin().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.entityBaseMapper.getResourceLocationProvider().get(
            this.getPlayerInfo(
                    this.gameProfile.getUniqueId()
            ).getLocationCape().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.entityBaseMapper.getResourceLocationProvider().get(
            this.getPlayerInfo(this.gameProfile.getUniqueId()).getLocationCape().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getSkinLocation() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getCloakLocation() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getElytraLocation() != null;
  }

  /**
   * Retrieves a {@link net.minecraft.client.network.play.NetworkPlayerInfo} with the given unique identifier.
   *
   * @param uniqueId The unique identifier of the profile
   * @return A {@link net.minecraft.client.network.play.NetworkPlayerInfo} or {@code null}
   */
  private net.minecraft.client.network.play.NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
    return Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId);
  }

}
