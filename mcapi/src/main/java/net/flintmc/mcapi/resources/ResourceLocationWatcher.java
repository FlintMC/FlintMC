package net.flintmc.mcapi.resources;

import java.util.function.Consumer;

public interface ResourceLocationWatcher {

  ResourceLocation getCurrentLocation();

  void registerLoadCallback(Consumer<ResourceLocation> loadCallback);

  boolean isLoaded();

}
