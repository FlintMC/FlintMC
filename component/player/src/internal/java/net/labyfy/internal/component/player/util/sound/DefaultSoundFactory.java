package net.labyfy.internal.component.player.util.sound;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.resources.ResourceLocationProvider;

/**
 * An implementation of {@link Sound.Factory}
 */
@Singleton
@Implement(Sound.Factory.class)
public class DefaultSoundFactory implements Sound.Factory {

    private final ResourceLocationProvider locationProvider;

    @Inject
    public DefaultSoundFactory(ResourceLocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    /**
     * Creates a new {@link Sound} with the given path.
     *
     * @param path The path of the sound.
     * @return The created sound.
     */
    @Override
    public Sound create(String path) {
        return () -> locationProvider.get(path);
    }
}
