package net.flintmc.mcapi.tileentity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * Represents the Minecraft sign tile entity.
 */
public interface SignTileEntity extends TileEntity {

  /**
   * Retrieves the text of the sign in the specified line.
   *
   * @param line The line of the sign to get the text.
   * @return The text of the sign in the specified line or {@code null}.
   */
  ChatComponent getText(int line);

  /**
   * Changes the text at the given line.
   *
   * @param line      The line to change the text.
   * @param component The new text for the line.
   */
  void setText(int line, ChatComponent component);

  /**
   * A factory for the {@link SignTileEntity}.
   */
  @AssistedFactory(SignTileEntity.class)
  interface Factory {

    /**
     * Creates a new {@link SignTileEntity} with the given non-null Minecraft sign tile entity.
     *
     * @param signTileEntity The non-null Minecraft sign tile entity.
     * @return A created sign tile entity.
     */
    SignTileEntity create(@Assisted("tileEntity") Object signTileEntity);
  }
}
