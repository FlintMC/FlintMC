package net.flintmc.mcapi.v1_15_2.world.storage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.mapper.WorldMapper;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.storage.service.WorldLoader;
import net.flintmc.mcapi.world.storage.service.exception.WorldLoadException;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;

@Singleton
@Implement(value = WorldLoader.class, version = "1.15.2")
public class VersionedWorldLoader implements WorldLoader {

  private final WorldMapper worldMapper;
  private final Map<String, WorldOverview> worldOverviews;
  private boolean loaded;

  @Inject
  private VersionedWorldLoader(WorldMapper worldMapper) {
    this.worldMapper = worldMapper;
    this.worldOverviews = Maps.newHashMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadWorlds() {
    try {
      this.convertWorldSummaries();
      this.loaded = true;
    } catch (AnvilConverterException e) {
      e.printStackTrace();
      this.loaded = false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WorldOverview> getWorlds() {
    if (this.loaded) {
      this.loaded = false;
      return new ArrayList<>(this.worldOverviews.values());
    }
    throw new WorldLoadException("The worlds are not loaded yet.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canLoadWorld(String fileName) {
    return Minecraft.getInstance().getSaveLoader().canLoadWorld(fileName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLoaded() {
    return this.loaded;
  }

  private void convertWorldSummaries() throws AnvilConverterException {
    SaveFormat saveFormat = Minecraft.getInstance().getSaveLoader();
    for (WorldSummary worldSummary : saveFormat.getSaveList()) {
      this.worldOverviews.put(
          worldSummary.getFileName(),
          this.worldMapper.fromMinecraftWorldSummary(worldSummary)
      );
    }
  }

}
