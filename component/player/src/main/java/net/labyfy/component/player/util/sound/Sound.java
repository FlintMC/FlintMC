package net.labyfy.component.player.util.sound;

import net.labyfy.component.resources.ResourceLocation;

/**
 * Represents a sound
 */
public interface Sound {

    /**
     * Retrieves the resource location of this sound.
     *
     * @return the resource location of this sound.
     */
    ResourceLocation getName();

    /**
     * A factory class for {@link Sound}
     */
    interface Factory {

        /**
         * Creates a new {@link Sound} with the given path.
         *
         * @param path The path of the sound.
         * @return the created sound.
         */
        Sound create(String path);
    }

}
