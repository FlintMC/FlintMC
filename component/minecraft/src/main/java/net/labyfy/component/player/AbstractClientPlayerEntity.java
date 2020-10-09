package net.labyfy.component.player;

import net.labyfy.component.player.network.NetworkPlayerInfo;

public interface AbstractClientPlayerEntity extends PlayerEntity, PlayerSkinProfile {

  boolean hasPlayerInfo();

  NetworkPlayerInfo getNetworkPlayerInfo();

  float getFovModifier();

}
