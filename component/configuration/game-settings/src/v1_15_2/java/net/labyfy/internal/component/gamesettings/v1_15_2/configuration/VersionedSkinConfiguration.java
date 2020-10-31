package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Singleton;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.gamesettings.configuration.SkinConfiguration;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.model.PlayerClothing;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.HandSide;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.15.2 implementation of {@link SkinConfiguration}.
 */
@Singleton
@ConfigImplementation(value = SkinConfiguration.class, version = "1.15.2")
public class VersionedSkinConfiguration implements SkinConfiguration {

  private static final PlayerClothing[] CLOTHINGS = PlayerClothing.values();

  /**
   * {@inheritDoc}
   */
/*  @Override
  public Set<PlayerClothing> getPlayerClothing() {
    return Minecraft.getInstance().gameSettings
        .getModelParts()
        .stream()
        .map(this::fromMinecraftObject)
        .collect(Collectors.toSet());
  }*/

  @Override
  public Map<PlayerClothing, Boolean> getAllModelClothingEnabled() {
    Map<PlayerClothing, Boolean> map = new HashMap<>();

    for (PlayerClothing clothing : CLOTHINGS) {
      map.put(clothing, this.isModelClothingEnabled(clothing));
    }

    return map;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModelClothingEnabled(PlayerClothing clothing, boolean enable) {
    Minecraft.getInstance().gameSettings.setModelPartEnabled(this.toMinecraftObject(clothing), enable);
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  @Override
  public void setAllModelClothingEnabled(Map<PlayerClothing, Boolean> map) {
    map.forEach(this::setModelClothingEnabled);
  }

  @Override
  public boolean isModelClothingEnabled(PlayerClothing clothing) {
    PlayerModelPart targetPart = this.toMinecraftObject(clothing);
    for (PlayerModelPart part : Minecraft.getInstance().gameSettings.getModelParts()) {
      if (part == targetPart) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void switchModelClothingEnabled(PlayerClothing clothing) {
    Minecraft.getInstance().gameSettings.switchModelPartEnabled(this.toMinecraftObject(clothing));
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getMainHand() {
    switch (Minecraft.getInstance().gameSettings.mainHand) {
      case LEFT:
        return Hand.Side.LEFT;
      case RIGHT:
        return Hand.Side.RIGHT;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.mainHand);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMainHand(Hand.Side mainHand) {
    switch (mainHand) {
      case LEFT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.LEFT;
        break;
      case RIGHT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.RIGHT;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mainHand);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  private PlayerClothing fromMinecraftObject(PlayerModelPart playerModelPart) {
    switch (playerModelPart) {
      case CAPE:
        return PlayerClothing.CLOAK;
      case JACKET:
        return PlayerClothing.JACKET;
      case LEFT_SLEEVE:
        return PlayerClothing.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerClothing.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerClothing.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerClothing.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerClothing.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + playerModelPart);
    }
  }

  private PlayerModelPart toMinecraftObject(PlayerClothing clothing) {
    switch (clothing) {
      case CLOAK:
        return PlayerModelPart.CAPE;
      case JACKET:
        return PlayerModelPart.JACKET;
      case LEFT_SLEEVE:
        return PlayerModelPart.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerModelPart.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerModelPart.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerModelPart.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerModelPart.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + clothing);
    }
  }
}
