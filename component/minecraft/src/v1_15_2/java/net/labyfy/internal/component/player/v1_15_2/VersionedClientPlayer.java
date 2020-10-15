package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryController;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

@Implement(value = ClientPlayer.class, version = "1.15.2")
public class VersionedClientPlayer implements ClientPlayer {

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
