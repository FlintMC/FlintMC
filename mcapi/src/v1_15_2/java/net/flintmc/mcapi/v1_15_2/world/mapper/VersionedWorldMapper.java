package net.flintmc.mcapi.v1_15_2.world.mapper;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldConfiguration;
import net.flintmc.mcapi.world.storage.WorldType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

@Singleton
@Implement(value = WorldMapper.class, version = "1.15.2")
public class VersionedWorldMapper implements WorldMapper {

  @Override
  public Object toMinecraftWorldSettings(WorldConfiguration configuration) {
    return new WorldSettings(
            configuration.getSeed(),
            GameType.getByName(configuration.getGameMode().getName()),
            configuration.isMapFeaturesEnabled(),
            configuration.isHardcoreMode(),
            (net.minecraft.world.WorldType) this.toMinecraftWorldType(configuration.getWorldType())
    );
  }

  @Override
  public Object toMinecraftWorldType(WorldType worldType) {
    return net.minecraft.world.WorldType.byName(worldType.getName());
  }
}
