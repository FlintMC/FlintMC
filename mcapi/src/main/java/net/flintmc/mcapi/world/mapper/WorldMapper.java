package net.flintmc.mcapi.world.mapper;

import net.flintmc.mcapi.world.storage.WorldConfiguration;
import net.flintmc.mcapi.world.storage.WorldType;

public interface WorldMapper {

  Object toMinecraftWorldSettings(WorldConfiguration configuration);

  Object toMinecraftWorldType(WorldType worldType);

}
