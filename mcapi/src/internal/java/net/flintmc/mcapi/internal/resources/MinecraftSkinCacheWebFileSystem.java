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

import net.flintmc.mcapi.internal.resources.SkinCacheWebResource.Factory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;

@Singleton
@WebFileSystem("minecraft-skin-cache")
public class MinecraftSkinCacheWebFileSystem implements WebFileSystemHandler {

  private final ResourceLocationProvider resourceLocationProvider;
  private final MinecraftSkinCacheDirectoryProvider minecraftSkinCacheDirectoryProvider;
  private final SkinCacheWebResource.Factory skinCacheWebResourceFactory;

  @Inject
  private MinecraftSkinCacheWebFileSystem(
      ResourceLocationProvider resourceLocationProvider,
      MinecraftSkinCacheDirectoryProvider minecraftSkinCacheDirectoryProvider,
      Factory skinCacheWebResourceFactory) {
    this.resourceLocationProvider = resourceLocationProvider;
    this.minecraftSkinCacheDirectoryProvider = minecraftSkinCacheDirectoryProvider;
    this.skinCacheWebResourceFactory = skinCacheWebResourceFactory;
  }

  @Override
  public boolean existsFile(String path) {
    return new File(this.minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path)
        .exists();
  }

  @Override
  public WebResource getFile(String path) throws FileNotFoundException {

    ResourceLocation resourceLocation = this.resourceLocationProvider.get(path);
    if (resourceLocation.exists()) {
      return new ResourcePackWebResource(resourceLocation);
    }
    path = path.replaceFirst("skins/", "");
    path = path.substring(0, 2) + "/" + path;
    File file = new File(this.minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path);
    if (!file.exists()) {
      throw new FileNotFoundException(
          "Skin " + path + " not found");
    }
    return skinCacheWebResourceFactory
        .create(minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path);
  }
}
