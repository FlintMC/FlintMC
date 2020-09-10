package net.labyfy.internal.component.player.v1_15_2.serializer.util.sound;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.util.sound.SoundCategory;

/**
 * 1.15.2 implementation of {@link SoundCategorySerializer}
 */
@Singleton
@Implement(value = SoundCategorySerializer.class, version = "1.15.2")
public class VersionedSoundCategorySerializer implements SoundCategorySerializer<net.minecraft.util.SoundCategory> {

    /**
     * Deserializes the Mojang {@link net.minecraft.util.SoundCategory} to the Labyfy {@link SoundCategory}
     *
     * @param value The sound category being deserialized
     * @return A deserialized {@link SoundCategory}
     */
    @Override
    public SoundCategory deserialize(net.minecraft.util.SoundCategory value) {
        switch (value) {
            case MASTER:
                return SoundCategory.MASTER;
            case MUSIC:
                return SoundCategory.MUSIC;
            case RECORDS:
                return SoundCategory.RECORD;
            case WEATHER:
                return SoundCategory.WEATHER;
            case BLOCKS:
                return SoundCategory.BLOCK;
            case HOSTILE:
                return SoundCategory.HOSTILE;
            case NEUTRAL:
                return SoundCategory.NEUTRAL;
            case PLAYERS:
                return SoundCategory.PLAYER;
            case AMBIENT:
                return SoundCategory.AMBIENT;
            case VOICE:
                return SoundCategory.VOICE;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the Labyfy {@link SoundCategory} to the Mojang {@link net.minecraft.util.SoundCategory}
     *
     * @param value The sound category being serialized
     * @return A serialized {@link net.minecraft.util.SoundCategory}
     */
    @Override
    public net.minecraft.util.SoundCategory serialize(SoundCategory value) {
        switch (value) {
            case MASTER:
                return net.minecraft.util.SoundCategory.MASTER;
            case MUSIC:
                return net.minecraft.util.SoundCategory.MUSIC;
            case RECORD:
                return net.minecraft.util.SoundCategory.RECORDS;
            case WEATHER:
                return net.minecraft.util.SoundCategory.WEATHER;
            case BLOCK:
                return net.minecraft.util.SoundCategory.BLOCKS;
            case HOSTILE:
                return net.minecraft.util.SoundCategory.HOSTILE;
            case NEUTRAL:
                return net.minecraft.util.SoundCategory.NEUTRAL;
            case PLAYER:
                return net.minecraft.util.SoundCategory.PLAYERS;
            case AMBIENT:
                return net.minecraft.util.SoundCategory.AMBIENT;
            case VOICE:
                return net.minecraft.util.SoundCategory.VOICE;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
