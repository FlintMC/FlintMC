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

package net.flintmc.mcapi.internal.resources;

import com.google.inject.Inject;
import java.io.FileNotFoundException;
import com.google.inject.Singleton;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;

@Singleton
@WebFileSystem("resource")
public class ResourcePackWebFileSystem implements WebFileSystemHandler {

  private final ResourceLocationProvider provider;

  @Inject
  private ResourcePackWebFileSystem(ResourceLocationProvider provider) {
    this.provider = provider;
  }

  /** {@inheritDoc} */
  @Override
  public boolean existsFile(String path) {
    return this.provider.get(path).exists();
  }

  /** {@inheritDoc} */
  @Override
  public WebResource getFile(String path) throws FileNotFoundException {
    ResourceLocation location = this.provider.get(path);
    if (!location.exists()) {
      throw new FileNotFoundException(
          "ResourceLocation " + location.getNamespace() + ":" + location.getPath() + " not found");
    }
    return new ResourcePackWebResource(location);
  }
}
