package net.flintmc.mcapi.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;

import java.io.InputStream;

/** The favicon of a server in the {@link ServerStatus}. */
public interface ServerFavicon {

  /**
   * Retrieves whether this favicon is a custom icon or the default minecraft icon.
   *
   * @return {@code true} if this favicon is custom, {@code false} otherwise
   */
  boolean isCustom();

  /**
   * Retrieves a new input stream with the data of this favicon, servers should send a PNG image
   * with the size of 64x64. This can only be {@code null} if the server didn't send a favicon and
   * an error occurred while loading the default favicon.
   *
   * @return The stream with the favicon or {@code null} if an error occurred while loading the
   *     default favicon
   */
  InputStream createStream();

  /** Factory for the {@link ServerFavicon}. */
  @AssistedFactory(ServerFavicon.class)
  interface Factory {

    /**
     * Creates a new favicon pointing to the given resource location.
     *
     * @param resourceLocation The non-null resource location of the icon
     * @return The new non-null favicon
     */
    ServerFavicon createDefault(@Assisted("resourceLocation") ResourceLocation resourceLocation);

    /**
     * Creates a new favicon pointing to the given PNG data.
     *
     * @param data The non-null PNG data of the image
     * @return The new non-null favicon
     */
    ServerFavicon createCustom(@Assisted("data") byte[] data);

    /**
     * Creates a new favicon pointing to the given PNG data encoded as Base64.
     *
     * @param base64Data The non-null PNG data of the image as Base64 data and prefixed with
     *     'data:image/png;base64,'
     * @return The new non-null favicon
     * @throws IllegalArgumentException If the given string is not a valid Base64 encoded string
     */
    ServerFavicon createCustom(@Assisted("base64Data") String base64Data)
        throws IllegalArgumentException;
  }
}
