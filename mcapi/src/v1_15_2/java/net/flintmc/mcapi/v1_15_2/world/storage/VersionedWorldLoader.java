package net.flintmc.mcapi.v1_15_2.world.storage;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.storage.WorldLoader;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;

import java.util.Collection;
import java.util.Map;

@Singleton
@Implement(value = WorldLoader.class, version = "1.15.2")
public class VersionedWorldLoader implements WorldLoader {

  private final WorldOverview.Factory worldOverviewFactory;
  private final Map<String, WorldOverview> worldOverviews;

  @Inject
  private VersionedWorldLoader(WorldOverview.Factory worldOverviewFactory) {
    this.worldOverviewFactory = worldOverviewFactory;
    this.worldOverviews = Maps.newHashMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadWorlds() {
    try {
      this.convertWorldSummaries();
    } catch (AnvilConverterException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<WorldOverview> getWorlds() {
    return this.worldOverviews.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canLoadWorld(String fileName) {
    return Minecraft.getInstance().getSaveLoader().canLoadWorld(fileName);
  }

  private void convertWorldSummaries() throws AnvilConverterException {
    SaveFormat saveFormat = Minecraft.getInstance().getSaveLoader();
    for (WorldSummary worldSummary : saveFormat.getSaveList()) {
      this.worldOverviews.put(
              worldSummary.getFileName(),
              this.worldOverviewFactory.create(
                      worldSummary.getFileName(),
                      worldSummary.getDisplayName(),
                      worldSummary.getLastTimePlayed(),
                      worldSummary.getSizeOnDisk(),
                      worldSummary.requiresConversion(),
                      GameMode.valueOf(worldSummary.getEnumGameType().name()),
                      worldSummary.isHardcoreModeEnabled(),
                      worldSummary.getCheatsEnabled(),
                      worldSummary.askToOpenWorld(),
                      worldSummary.markVersionInList(),
                      worldSummary.func_202842_n(),
                      worldSummary.func_197731_n()
              )
      );
    }
  }

}
