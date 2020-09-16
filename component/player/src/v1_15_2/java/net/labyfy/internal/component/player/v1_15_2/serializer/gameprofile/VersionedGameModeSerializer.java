package net.labyfy.internal.component.player.v1_15_2.serializer.gameprofile;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.util.GameMode;
import net.minecraft.world.GameType;

/**
 * 1.15.2 implementation of {@link GameModeSerializer}
 */
@Singleton
@Implement(value = GameModeSerializer.class, version = "1.15.2")
public class VersionedGameModeSerializer implements GameModeSerializer<GameType> {

    /**
     * Deserializes the Labyfy {@link GameType} to the {@link GameMode}
     *
     * @param value The game type being deserialize
     * @return A deserialized {@link GameMode}
     */
    @Override
    public GameMode deserialize(GameType value) {
        switch (value) {
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            case NOT_SET:
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the Labyfy {@link GameMode} to the {@link GameType}
     *
     * @param value The game mode being serialized
     * @return A serialized game type
     */
    @Override
    public GameType serialize(GameMode value) {
        switch (value) {
            case SURVIVAL:
                return GameType.SURVIVAL;
            case CREATIVE:
                return GameType.CREATIVE;
            case ADVENTURE:
                return GameType.ADVENTURE;
            case SPECTATOR:
                return GameType.SPECTATOR;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
