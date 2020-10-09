package net.labyfy.component.player;

import net.labyfy.component.player.overlay.TabOverlay;

public interface ClientPlayerEntity extends AbstractClientPlayerEntity {

  void sendChatMessage(String message);

  void closeScreenAndDropStack();

  void setPlayerSPHealth(float health);

  void sendHorseInventory();

  String getServerBrand();

  void setServerBrand(String serverBrand);

  void setPermissionLevel(int level);

  void setXPStats(float f, int i, int i2);

  boolean isShowDeathScreen();

  void setShowDeathScreen(boolean showDeathScreen);

  boolean isRidingHorse();

  float getHorseJumpPower();

  boolean isRowingBoat();

  boolean isAutoJumpEnabled();

  float getWaterBrightness();

  TabOverlay getTabOverlay();

}
