package net.labyfy.component.resources.v1_15_2;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 1.15.2 implementation of the {@link ResourceLocationProvider}
 */
@Singleton
@Implement(value = ResourceLocationProvider.class, version = "1.15.2")
public class LabyResourceLocationProvider implements ResourceLocationProvider {

  private final LabyResourceLocation.Factory resourceLocationFactory;

  @Inject
  private LabyResourceLocationProvider(ResourceLocation.Factory resourceLocationFactory) {
    this.resourceLocationFactory = resourceLocationFactory;
  }

  /**
   * {@inheritDoc}
   */
  public ResourceLocation get(String path) {
    return this.get("minecraft", path);
  }

  /**
   * {@inheritDoc}
   */
  public ResourceLocation get(String nameSpace, String path) {
    return this.resourceLocationFactory.create(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation) throws IOException {
    return Minecraft.getInstance().getResourceManager()
        .getAllResources(resourceLocation.getHandle()).stream()
        .map(IResource::getLocation)
        .map(location -> get(location.getNamespace(), location.getPath()))
        .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getLoaded(String namespace) {
    return this.getLoaded(namespace, s -> true);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate) {
    return Minecraft.getInstance().getResourceManager()
        .getAllResourceLocations(namespace, predicate).stream()
        .map(location -> get(location.getNamespace(), location.getPath()))
        .collect(Collectors.toSet());
  }
}
