package net.flintmc.mcapi.v1_15_2.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.mcapi.player.overlay.TabOverlay;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

@Singleton
@Implement(value = ClientPlayer.class, version = "1.15.2")
public class VersionedClientPlayer implements ClientPlayer {

  private static final String UNKNOWN_BIOME = "Unknown";

  private final PlayerEntity.Provider playerEntityProvider;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;
  private final InventoryController inventoryController;
  private final TabOverlay tabOverlay;

  private PlayerEntity playerEntity;
  private ClientPlayerEntity clientPlayerEntity;

  @Inject
  private VersionedClientPlayer(
          PlayerEntity.Provider playerEntityProvider,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          InventoryController inventoryController,
          TabOverlay tabOverlay
  ) {
    this.playerEntityProvider = playerEntityProvider;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.inventoryController = inventoryController;
    this.tabOverlay = tabOverlay;

    this.clientPlayerEntity = Minecraft.getInstance().player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraPitch() {
    return Minecraft.getInstance().player.rotateElytraX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraPitch(float elytraPitch) {
    Minecraft.getInstance().player.rotateElytraX = elytraPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraYaw() {
    return Minecraft.getInstance().player.rotateElytraY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraYaw(float elytraYaw) {
    Minecraft.getInstance().player.rotateElytraY = elytraYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraRoll() {
    return Minecraft.getInstance().player.rotateElytraZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraRoll(float elytraRoll) {
    Minecraft.getInstance().player.rotateElytraZ = elytraRoll;
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
    return this.getNetworkPlayerInfo() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(Minecraft.getInstance().player.getGameProfile().getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return Minecraft.getInstance().player.getFovModifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerEntity getEntity() {

    if (this.clientPlayerEntity != Minecraft.getInstance().player) {
      this.clientPlayerEntity = Minecraft.getInstance().player;
      this.playerEntity = this.playerEntityProvider.get(this.clientPlayerEntity);
    }

    if (this.playerEntity == null) {
      this.playerEntity = this.playerEntityProvider.get(Minecraft.getInstance().player);
    }

    return this.playerEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerInventory getInventoryController() {
    return this.inventoryController.getPlayerInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Inventory getOpenInventory() {
    return this.inventoryController.getOpenInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendChatMessage(String message) {
    Minecraft.getInstance().player.sendChatMessage(message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeScreenAndDropStack() {
    Minecraft.getInstance().player.closeScreenAndDropStack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlayerSPHealth(float health) {
    Minecraft.getInstance().player.setPlayerSPHealth(health);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendHorseInventory() {
    Minecraft.getInstance().player.sendHorseInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerBrand() {
    return Minecraft.getInstance().player.getServerBrand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setServerBrand(String serverBrand) {
    Minecraft.getInstance().player.setServerBrand(serverBrand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionLevel(int level) {
    Minecraft.getInstance().player.setPermissionLevel(level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setExperienceStats(float currentExperience, int maxExperience, int level) {
    Minecraft.getInstance().player.setXPStats(currentExperience, maxExperience, level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDeathScreen() {
    return Minecraft.getInstance().player.isShowDeathScreen();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDeathScreen(boolean showDeathScreen) {
    Minecraft.getInstance().player.setShowDeathScreen(showDeathScreen);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRidingHorse() {
    return Minecraft.getInstance().player.isRidingHorse();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHorseJumpPower() {
    return Minecraft.getInstance().player.getHorseJumpPower();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRowingBoat() {
    return Minecraft.getInstance().player.isRowingBoat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAutoJumpEnabled() {
    return Minecraft.getInstance().player.isAutoJumpEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWaterBrightness() {
    return Minecraft.getInstance().player.getWaterBrightness();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TabOverlay getTabOverlay() {
    return this.tabOverlay;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRenderArmYaw() {
    return Minecraft.getInstance().player.renderArmYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRenderArmYaw(float renderArmYaw) {
    Minecraft.getInstance().player.renderArmYaw = renderArmYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPreviousRenderArmYaw() {
    return Minecraft.getInstance().player.prevRenderArmYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPreviousArmYaw(float previousRenderArmYaw) {
    Minecraft.getInstance().player.prevRenderArmYaw = previousRenderArmYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRenderArmPitch() {
    return Minecraft.getInstance().player.renderArmPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRenderArmPitch(float renderArmPitch) {
    Minecraft.getInstance().player.renderArmPitch = renderArmPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPreviousRenderArmPitch() {
    return Minecraft.getInstance().player.prevRenderArmPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPreviousRenderArmPitch(float previousRenderArmPitch) {
    Minecraft.getInstance().player.prevRenderArmPitch = previousRenderArmPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBiome() {
    World world = Minecraft.getInstance().world;
    Entity renderViewEntity = Minecraft.getInstance().getRenderViewEntity();

    if (renderViewEntity == null) {
      return UNKNOWN_BIOME;
    }

    BlockPos blockPos = new BlockPos(renderViewEntity);

    if (world != null) {
      String biomePath = Registry.BIOME.getKey(world.getBiome(blockPos)).getPath();
      String[] split = biomePath.split("_");

      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < split.length; i++) {
        String biomeName = split[i];
        biomeName = biomePath.substring(0, 1).toUpperCase() + biomeName.substring(1).toLowerCase();

        if (i == split.length - 1) {
          builder.append(biomeName);
          break;
        }

        builder.append(biomeName).append(" ");
      }
      return builder.toString();
    }

    return UNKNOWN_BIOME;
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
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }
}
