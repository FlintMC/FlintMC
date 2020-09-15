package net.labyfy.internal.component.player.v1_15_2.serializer.util;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.SkinModel;

/**
 * 1.15.2 implementation of {@link SkinModelSerializer}
 */
@Singleton
@Implement(value = SkinModelSerializer.class, version = "1.15.2")
public class VersionedSkinModelSerializer implements SkinModelSerializer<String> {

    /**
     * Deserializes the internal Mojang skin name to the {@link SkinModel}
     *
     * @param value The skin name being deserialized
     * @return A deserialized {@link SkinModel}
     */
    @Override
    public SkinModel deserialize(String value) {
        value = value.toLowerCase();
        switch (value) {
            case "default":
                return SkinModel.STEVE;
            case "slim":
                return SkinModel.ALEX;
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    /**
     * Serializes the {@link SkinModel} to the internal Mojang skin name.
     *
     * @param value The skin model being serialized
     * @return A serialized skin name
     */
    @Override
    public String serialize(SkinModel value) {
        return value.getModel();
    }
}
