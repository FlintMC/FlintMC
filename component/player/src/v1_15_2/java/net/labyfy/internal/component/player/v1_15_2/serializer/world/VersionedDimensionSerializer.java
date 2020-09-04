package net.labyfy.internal.component.player.v1_15_2.serializer.world;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.Dimension;
import net.minecraft.world.dimension.DimensionType;

/**
 * 1.15.2 implementation of {@link DimensionSerializer}
 */
@Implement(value = DimensionSerializer.class, version = "1.15.2")
public class VersionedDimensionSerializer implements DimensionSerializer<DimensionType> {

    /**
     * Deserializes the Mojang {@link DimensionType} to the Labyfy {@link Dimension}
     *
     * @param value The dimension type being deserialized
     * @return a deserialized {@link Dimension}
     */
    @Override
    public Dimension deserialize(DimensionType value) {
        switch (value.getId()) {
            case -1:
                return Dimension.NETHER;
            case 0:
                return Dimension.OVERWORLD;
            case 1:
                return Dimension.THE_END;
            default:
                throw new IllegalStateException("Unexpected value: " + value.getId());
        }
    }

    /**
     * Serializes the Labyfy {@link Dimension} to the Mojang {@link DimensionType}
     *
     * @param value The dimension being serialized
     * @return a serialized {@link DimensionType}
     */
    @Override
    public DimensionType serialize(Dimension value) {
        return DimensionType.getById(value.getId());
    }
}
