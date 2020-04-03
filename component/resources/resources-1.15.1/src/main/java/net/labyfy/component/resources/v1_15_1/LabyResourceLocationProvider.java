package net.labyfy.component.resources.v1_15_1;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
@Implement(value = ResourceLocationProvider.class, version = "1.15.1")
public class LabyResourceLocationProvider implements ResourceLocationProvider {

  private final LabyResourceLocation.Factory resourceLocationFactory;

  @Inject
  private LabyResourceLocationProvider(ResourceLocation.Factory resourceLocationFactory) {
    this.resourceLocationFactory = resourceLocationFactory;
  }

  public ResourceLocation get(String path) {
    return this.get("minecraft", path);
  }

  public ResourceLocation get(String nameSpace, String path) {
    return this.resourceLocationFactory.create(path);
  }

  public Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation) {
    try {
      return Minecraft.getInstance().getResourceManager()
          .getAllResources(resourceLocation.getHandle()).stream()
          .map(IResource::getLocation)
          .map(location -> get(location.getNamespace(), location.getPath()))
          .collect(Collectors.toSet());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Collections.emptyList();
  }

  public Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate) {
    return Minecraft.getInstance().getResourceManager()
        .getAllResourceLocations(namespace, predicate).stream()
        .map(location -> get(location.getNamespace(), location.getPath()))
        .collect(Collectors.toSet());
  }
}
