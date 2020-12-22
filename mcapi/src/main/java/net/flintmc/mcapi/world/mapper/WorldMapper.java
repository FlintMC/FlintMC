package net.flintmc.mcapi.world.mapper;

import net.flintmc.mcapi.world.storage.WorldConfiguration;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Mapper between the Minecraft world and Flint world.
 */
public interface WorldMapper {

  /**
   * Creates a new Minecraft world settings by using the Flint {@link WorldConfiguration} as the
   * base.
   *
   * @param configuration The non-null Flint {@link WorldConfiguration}.
   * @return The new Minecraft world settings or {@code null} if the given world configuration was
   * invalid.
   */
  Object toMinecraftWorldSettings(WorldConfiguration configuration);

  /**
   * Creates a new {@link WorldConfiguration} by using the given Minecraft world settings as the
   * base.
   *
   * @param handle The non-null Minecraft world settings.
   * @return The new Flint {@link WorldConfiguration} or {@code null} if the given world settings
   * was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft world settings.
   */
  WorldConfiguration fromMinecraftWorldSettings(Object handle);

  /**
   * Creates a new Minecraft world type by using the Flint {@link WorldType} as the base.
   *
   * @param worldType The non-null Flint {@link WorldType}.
   * @return The new Minecraft world type or {@code null} if the given world type was invalid.
   */
  Object toMinecraftWorldType(WorldType worldType);

  /**
   * Creates a new {@link WorldType} by using the given Minecraft world type as the base.
   *
   * @param handle The non-null Minecraft world type.
   * @return The new Flint {@link WorldType} or {@code null} if the given world type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft world type.
   */
  WorldType fromMinecraftWorldType(Object handle);

  /**
   * Creates a new Minecraft world summary by using the Flint {@link WorldOverview} as the base.
   *
   * @param worldOverview The non-null Flint {@link WorldOverview}.
   * @return The new Minecraft world summary or {@code null} if the given world summary was invalid.
   */
  Object toMinecraftWorldSummary(WorldOverview worldOverview);

  /**
   * Creates a new {@link WorldOverview} by using the given Minecraft world summary as the base.
   *
   * @param handle The non-null Minecraft world type.
   * @return The new Flint {@link WorldOverview} or {@code null} if the given world summary was
   * invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft world summary.
   */
  WorldOverview fromMinecraftWorldSummary(Object handle);

}
