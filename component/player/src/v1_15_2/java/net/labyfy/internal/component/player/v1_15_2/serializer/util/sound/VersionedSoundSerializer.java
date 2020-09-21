package net.labyfy.internal.component.player.v1_15_2.serializer.util.sound;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;

/**
 * 1.15.2 implementation of {@link SoundSerializer}
 */
@Singleton
@Implement(value = SoundSerializer.class, version = "1.15.2")
public class VersionedSoundSerializer implements SoundSerializer<SoundEvent> {

    private final ResourceLocationProvider locationProvider;
    private final Sound.Factory soundFactory;

    @Inject
    public VersionedSoundSerializer(
            ResourceLocationProvider locationProvider,
            Sound.Factory soundFactory
    ) {
        this.locationProvider = locationProvider;
        this.soundFactory = soundFactory;
    }

    /**
     * Deserializes the {@link SoundEvent} to the {@link Sound}
     *
     * @param value The sound event being deserialized
     * @return A deserialized {@link Sound}
     */
    @Override
    public Sound deserialize(SoundEvent value) {
        return this.soundFactory.create(value.getName().getPath());
    }

    /**
     * Serializes the {@link Sound} to the {@link SoundEvent}
     *
     * @param value The sound being serialized
     * @return A serialized {@link SoundEvent}
     */
    @Override
    public SoundEvent serialize(Sound value) {
        return Registry.register(
                Registry.SOUND_EVENT,
                value.getName().getPath(),
                new SoundEvent(
                        this.locationProvider.get(
                                value.getName().getPath()
                        ).getHandle()
                )
        );
    }
}
