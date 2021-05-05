package net.flintmc.mcapi.v1_15_2.player.skin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.skin.SkinProvider;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;
import net.flintmc.mcapi.v1_15_2.player.skin.VersionedSkinLoadingTask.Factory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Singleton
@Implement(SkinProvider.class)
public class VersionedSkinProvider implements SkinProvider {

  private final VersionedSkinLoadingTask.Factory versionedSkinLoadingTaskFactory;
  private final VersionedHeadSkinLoadingTask.Factory versionedHeadSkinLoadingTaskFactory;
  private final Map<GameProfile, ResourceLocationWatcher> watchers = new ConcurrentHashMap<>();
  private final ResourceLocation.Factory resourceLocationFactory;

  @Inject
  private VersionedSkinProvider(
      Factory versionedSkinLoadingTaskFactory,
      VersionedHeadSkinLoadingTask.Factory versionedHeadSkinLoadingTaskFactory,
      ResourceLocation.Factory resourceLocationFactory) {
    this.versionedSkinLoadingTaskFactory = versionedSkinLoadingTaskFactory;
    this.versionedHeadSkinLoadingTaskFactory = versionedHeadSkinLoadingTaskFactory;
    this.resourceLocationFactory = resourceLocationFactory;
  }

  @Override
  public ResourceLocationWatcher loadSkin(GameProfile gameProfile) {
    return this.loadSkin(gameProfile, resourceLocation -> {
    });
  }

  @Override
  public ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile) {
    return this.loadHeadSkin(gameProfile, resourceLocation -> {});
  }

  @Override
  public ResourceLocationWatcher loadSkin(GameProfile gameProfile,
      Consumer<ResourceLocation> updateCallback) {
    ResourceLocationWatcher resourceLocationWatcher = this.watchers
        .computeIfAbsent(gameProfile, key -> {
          VersionedSkinLoadingTask versionedSkinLoadingTask = versionedSkinLoadingTaskFactory
              .create(gameProfile);
          versionedSkinLoadingTask.registerLoadCallback(updateCallback);
          versionedSkinLoadingTask.registerLoadCallback(resourceLocation -> {
            this.watchers.remove(gameProfile);
          });
          versionedSkinLoadingTask.load();
          return versionedSkinLoadingTask;
        });
    if(resourceLocationWatcher.isLoaded()){
      this.watchers.remove(gameProfile);
    }
    return resourceLocationWatcher;
  }

  @Override
  public ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile,
      Consumer<ResourceLocation> updateCallback) {
    ResourceLocationWatcher resourceLocationWatcher = this.watchers
        .computeIfAbsent(gameProfile, key -> {
          VersionedHeadSkinLoadingTask versionedHeadSkinLoadingTask = versionedHeadSkinLoadingTaskFactory
              .create(gameProfile);
          versionedHeadSkinLoadingTask.registerLoadCallback(updateCallback);
          versionedHeadSkinLoadingTask.registerLoadCallback(resourceLocation -> {
            this.watchers.remove(gameProfile);
          });
          versionedHeadSkinLoadingTask.load();
          return versionedHeadSkinLoadingTask;
        });
    if(resourceLocationWatcher.isLoaded()){
      this.watchers.remove(gameProfile);
    }
    return resourceLocationWatcher;
  }
}
