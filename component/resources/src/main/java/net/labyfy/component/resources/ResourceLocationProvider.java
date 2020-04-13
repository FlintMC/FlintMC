package net.labyfy.component.resources;

import java.util.Collection;
import java.util.function.Predicate;

public interface ResourceLocationProvider {

  ResourceLocation get(String path);

  ResourceLocation get(String nameSpace, String path);

  Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation);

  Collection<ResourceLocation> getLoaded(String namespace);

  Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate);

  <T extends WrappedResourceLocation> T wrap(ResourceLocation resourceLocation, Class<T> clazz);

}
