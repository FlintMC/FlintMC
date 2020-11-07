package net.flintmc.mcapi.tileentity.mapper;

import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.TileEntity;

/** Mapper between the Minecraft tile entity type and Flint tile entity. */
public interface TileEntityMapper {

  /**
   * Retrieves a Minecraft tile entity by using the Flint {@link TileEntity} as the base.
   *
   * @param tileEntity The non-null Flint {@link TileEntity}.
   * @return A Minecraft tile entity or {@code null} if the given tile entity was invalid.
   */
  Object toMinecraftTileEntity(TileEntity tileEntity);

  /**
   * Creates a new {@link TileEntity} by using the given Minecraft tile entity as the base.
   *
   * @param tileEntity The non-null Minecraft tile entity.
   * @return The new Flint {@link TileEntity} or {@code null} if the given tile entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft tile entity.
   */
  TileEntity fromMinecraftTileEntity(Object tileEntity);

  /**
   * Retrieves a Minecraft sign tile entity by using the Flint {@link SignTileEntity} as the base.
   *
   * @param signTileEntity The non-null Flint {@link SignTileEntity}.
   * @return A Minecraft sign tile entity or {@code null} if the given sign tile entity was invalid.
   */
  Object toMinecraftSignTileEntity(SignTileEntity signTileEntity);

  /**
   * Creates a new {@link SignTileEntity} by using the given Minecraft sign tile entity as the base.
   *
   * @param signTileEntity The non-null Minecraft sign tile entity.
   * @return The new Flint {@link SignTileEntity} or {@code null} if the given sign tile entity was
   *     invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft sign tile entity.
   */
  SignTileEntity fromMinecraftSignTileEntity(Object signTileEntity);
}
