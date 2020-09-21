package net.labyfy.internal.component.player.v1_15_2.serializer.util;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.HandSideSerializer;
import net.labyfy.component.player.util.Hand;
import net.minecraft.util.HandSide;

/**
 * 1.15.2 implementation of {@link HandSideSerializer}
 */
@Implement(value = HandSideSerializer.class, version = "1.15.2")
public class VersionedHandSideSerializer implements HandSideSerializer<HandSide> {

    /**
     * Deserializes the {@link HandSide} to the {@link Hand.Side}
     *
     * @param value The hand side being deserialized
     * @return A deserialized {@link Hand.Side}
     */
    @Override
    public Hand.Side deserialize(HandSide value) {
        switch (value) {
            case LEFT:
                return Hand.Side.LEFT;
            case RIGHT:
                return Hand.Side.RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the {@link Hand.Side} to the {@link HandSide}
     *
     * @param value The hand side being serialized
     * @return A serialized {@link HandSide}
     */
    @Override
    public HandSide serialize(Hand.Side value) {
        switch (value) {
            case LEFT:
                return HandSide.LEFT;
            case RIGHT:
                return HandSide.RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}
