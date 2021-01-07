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

package net.flintmc.mcapi.internal.server.status;

import com.google.common.base.Charsets;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.server.status.ServerFavicon;
import org.apache.logging.log4j.Logger;

@Implement(ServerFavicon.class)
public class DefaultServerFavicon implements ServerFavicon {

  public static final String PREFIX = "data:image/png;base64,";

  private final Logger logger;
  private final ResourceLocation defaultServerIcon;
  private final byte[] data;

  @AssistedInject
  public DefaultServerFavicon(
      @InjectLogger Logger logger,
      @Assisted("resourceLocation") ResourceLocation defaultServerIcon) {
    this(logger, defaultServerIcon, null);
  }

  @AssistedInject
  public DefaultServerFavicon(@Assisted("base64Data") String base64Data)
      throws IllegalArgumentException {
    this(
        Base64.getDecoder()
            .decode(
                base64Data
                    .substring(PREFIX.length())
                    .replaceAll("\n", "")
                    .getBytes(Charsets.UTF_8)));
  }

  @AssistedInject
  public DefaultServerFavicon(@Assisted("data") byte[] data) {
    this(null, null, data);
  }

  private DefaultServerFavicon(Logger logger, ResourceLocation defaultServerIcon, byte[] data) {
    this.logger = logger;
    this.defaultServerIcon = defaultServerIcon;
    this.data = data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCustom() {
    return this.data != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream createStream() {
    if (this.data == null) {
      try {
        return this.defaultServerIcon.openInputStream();
      } catch (IOException exception) {
        this.logger.trace("Exception while loading the default server icon", exception);
        return null;
      }
    }

    return new ByteArrayInputStream(this.data);
  }
}
