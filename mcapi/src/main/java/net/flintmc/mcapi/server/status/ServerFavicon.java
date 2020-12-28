/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.server.status;

import java.io.InputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * The favicon of a server in the {@link ServerStatus}.
 */
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
   * default favicon
   */
  InputStream createStream();

  /**
   * Factory for the {@link ServerFavicon}.
   */
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
     *                   'data:image/png;base64,'
     * @return The new non-null favicon
     * @throws IllegalArgumentException If the given string is not a valid Base64 encoded string
     */
    ServerFavicon createCustom(@Assisted("base64Data") String base64Data)
        throws IllegalArgumentException;
  }
}
