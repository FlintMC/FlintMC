package net.flintmc.mcapi.resources;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import java.io.IOException;
import java.io.InputStream;

/** Represents a resource location of minecraft. */
public interface ResourceLocation {

  /**
   * Retrieves a the underlying vanilla object. This should only be used in versioned code since
   * this function is unsafe and may cause unexpected {@link ClassCastException}s.
   *
   * @return The vanilla object
   */
  <T> T getHandle();

  /**
   * Retrieves the path of the resource location within the resource pack.
   *
   * @return The path of the resource location
   */
  String getPath();

  /**
   * Retrieves the namespace the resource location is situated in.
   *
   * @return The namespace the resource location is in
   */
  String getNamespace();

  /**
   * Opens an input stream to this resource location using the currently selected resource packs.
   *
   * @return An input stream pointing to the resource location
   * @throws java.io.FileNotFoundException If the resource location can't be found in any selected
   *     pack
   * @throws IOException If an I/O error occurs
   */
  InputStream openInputStream() throws IOException;

  /**
   * Retrieves whether this resource location exists in any resource pack.
   *
   * @return {@code true} if it exists, {@code false} otherwise
   */
  boolean exists();

  /** Factory class for {@link ResourceLocation} */
  @AssistedFactory(ResourceLocation.class)
  interface Factory {

    /**
     * Creates a new {@link ResourceLocation} with the given path where the namespace is extracted
     * from the path string itself. The rules of vanilla resource location naming apply.
     *
     * @param fullPath A path string in the format {@code "namespace:location"} or {@code
     *     "location"} for the minecraft namespace
     * @return The created resource location
     */
    ResourceLocation create(@Assisted("fullPath") String fullPath);

    /**
     * Creates a new {@link ResourceLocation} within the given namespace pointing to the path given.
     * The rules of vanilla resource location naming apply.
     *
     * @param nameSpace The namespace this resource location is situated in
     * @param path The path to this resource location within the namespace
     * @return The created resource location
     */
    ResourceLocation create(@Assisted("nameSpace") String nameSpace, @Assisted("path") String path);
  }
}
