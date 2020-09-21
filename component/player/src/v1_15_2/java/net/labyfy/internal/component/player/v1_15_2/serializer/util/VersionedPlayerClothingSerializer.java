package net.labyfy.internal.component.player.v1_15_2.serializer.util;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.PlayerClothingSerializer;
import net.labyfy.component.player.util.PlayerClothing;
import net.minecraft.entity.player.PlayerModelPart;

/**
 * 1.15.2 implementation of {@link PlayerClothingSerializer}
 */
@Singleton
@Implement(value = PlayerClothingSerializer.class, version = "1.15.2")
public class VersionedPlayerClothingSerializer implements PlayerClothingSerializer<PlayerModelPart> {

    /**
     * Deserializes the {@link PlayerModelPart} to the {@link PlayerClothing}
     *
     * @param value The player model part being deserialized
     * @return A deserialized {@link PlayerClothing}
     */
    @Override
    public PlayerClothing deserialize(PlayerModelPart value) {
        switch (value) {
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
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the {@link PlayerClothing} to the {@link PlayerModelPart}
     *
     * @param value The player clothing being serialized
     * @return A serialized {@link PlayerModelPart}
     */
    @Override
    public PlayerModelPart serialize(PlayerClothing value) {
        switch (value) {
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
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
