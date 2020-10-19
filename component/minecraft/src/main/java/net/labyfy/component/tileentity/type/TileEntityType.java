package net.labyfy.component.tileentity.type;

import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents a Minecraft tile entity type.
 */
public interface TileEntityType {

  /**
   * A factory class for the {@link TileEntityType}.
   */
  @AssistedFactory(TileEntityType.class)
  interface Factory {

    /**
     * Creates a new {@link TileEntityType}.
     *
     * @return A created tile entity type.
     */
    TileEntityType create();

  }

}
