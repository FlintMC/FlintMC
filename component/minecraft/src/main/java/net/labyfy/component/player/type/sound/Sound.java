package net.labyfy.component.player.type.sound;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocation;

/**
 * Represents a Minecraft sound.
 */
public interface Sound {

  /**
   * Retrieves the resource location of this sound.
   *
   * @return The resource location of this sound.
   */
  ResourceLocation getName();

  /**
   * A factory class for {@link Sound}
   */
  @AssistedFactory(Sound.class)
  interface Factory {

    /**
     * Creates a new {@link Sound} with the given path.
     *
     * @param path The path of the sound.
     * @return The created sound.
     */
    Sound create(@Assisted("path") String path);
  }

}
