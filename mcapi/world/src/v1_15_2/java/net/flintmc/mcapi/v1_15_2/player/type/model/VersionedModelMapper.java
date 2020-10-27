package net.flintmc.mcapi.v1_15_2.player.type.model;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.minecraft.entity.player.PlayerModelPart;

@Singleton
@Implement(value = ModelMapper.class, version = "1.15.2")
public class VersionedModelMapper implements ModelMapper {

  @Override
  public PlayerClothing fromMinecraftPlayerModelPart(Object playerModelPart) {
    if (!(playerModelPart instanceof PlayerModelPart)) {
      throw new IllegalArgumentException("");
    }

    PlayerModelPart modelPart = (PlayerModelPart) playerModelPart;

    switch (modelPart) {
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
        throw new IllegalStateException("Unexpected value: " + modelPart);
    }
  }

  @Override
  public Object toMinecraftPlayerModelPart(PlayerClothing playerClothing) {
    switch (playerClothing) {
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
        throw new IllegalStateException("Unexpected value: " + playerClothing);
    }
  }
}
