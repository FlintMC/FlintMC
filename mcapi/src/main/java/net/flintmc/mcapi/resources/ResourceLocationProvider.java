package net.flintmc.mcapi.resources;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;

/** Service interface for creating {@link ResourceLocation}s. */
public interface ResourceLocationProvider {
  /**
   * Creates a resource location with the given path. Equivalent to {@link
   * ResourceLocation.Factory#create(String)}.
   *
   * @param path The path of the resource location, see {@link
   *     ResourceLocation.Factory#create(String)} for more information
   * @return The created resource location
   * @see ResourceLocation.Factory#create(String)
   */
  ResourceLocation get(String path);

  /**
   * Creates a resource location within the given namespace and with the given path. Equivalent to
   * {@link ResourceLocation.Factory#create(String, String)}
   *
   * @param nameSpace The namespace of the resource location, see {@link
   *     ResourceLocation.Factory#create(String, String)} for more information
   * @param path The path of the resource location, see {@link
   *     ResourceLocation.Factory#create(String, String)} for more information
   * @return The created resource location
   */
  ResourceLocation get(String nameSpace, String path);

  /**
   * Recursively collects all resource locations located under the given location.
   *
   * @param resourceLocation The location to search for children
   * @return A collection of all found children
   * @throws java.io.FileNotFoundException If the given resource location does not exist
   * @throws IOException If an I/O error occurs
   */
  Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation) throws IOException;

  /**
   * Retrieves all currently loaded resource locations within the given namespace.
   *
   * @param namespace The namespace to search for loaded resources
   * @return A collection of all currently loaded resource locations within the namespace
   */
  Collection<ResourceLocation> getLoaded(String namespace);

  /**
   * Retrieves currently loaded resources matching a predicate.
   *
   * @param namespace The namespace to search for loaded resources
   * @param predicate The predicate to use for matching locations
   * @return A collection of all currently loaded resource locations matching the given predication
   *     within the namespace
   */
  Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate);
}
