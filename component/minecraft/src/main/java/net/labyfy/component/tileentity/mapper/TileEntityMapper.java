package net.labyfy.component.tileentity.mapper;

import net.labyfy.component.tileentity.SignTileEntity;

/**
 * Mapper between the Minecraft tile entity type and Labyfy tile entity.
 */
public interface TileEntityMapper {

  /**
   * Retrieves a Minecraft sign tile entity by using the Labyfy {@link SignTileEntity} as the base.
   *
   * @param signTileEntity The non-null Labyfy {@link SignTileEntity}.
   * @return A Minecraft sign tile entity or {@code null} if the given sign tile entity was invalid.
   */
  Object toMinecraftSignTileEntity(SignTileEntity signTileEntity);

  /**
   * Creates a new {@link SignTileEntity} by using the given Minecraft sign tile entity as the base.
   *
   * @param signTileEntity The non-null Minecraft sign tile entity.
   * @return The new Labyfy {@link SignTileEntity} or {@code null} if the given sign tile entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft sign tile entity.
   */
  SignTileEntity fromMinecraftSignTileEntity(Object signTileEntity);

}
