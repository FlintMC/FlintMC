package net.labyfy.internal.component.player.v1_15_2.serializer.util;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.HandSerializer;
import net.labyfy.component.player.util.Hand;

/**
 * 1.15.2 implementation of {@link HandSerializer}
 */
@Singleton
@Implement(value = HandSerializer.class, version = "1.15.2")
public class VersionedHandSerializer implements HandSerializer<net.minecraft.util.Hand> {

    /**
     * Deserializes the Mojang {@link net.minecraft.util.Hand} to the Labyfy {@link Hand}
     *
     * @param value The hand being deserialized
     * @return A deserialized {@link Hand}
     */
    @Override
    public Hand deserialize(net.minecraft.util.Hand value) {
        switch (value) {
            case MAIN_HAND:
                return Hand.MAIN_HAND;
            case OFF_HAND:
                return Hand.OFF_HAND;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the Labyfy {@link Hand} to the {@link net.minecraft.util.Hand}
     *
     * @param value The hand being serialized
     * @return A serialized {@link net.minecraft.util.Hand}
     */
    @Override
    public net.minecraft.util.Hand serialize(Hand value) {
        switch (value) {
            case MAIN_HAND:
                return net.minecraft.util.Hand.MAIN_HAND;
            case OFF_HAND:
                return net.minecraft.util.Hand.OFF_HAND;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
