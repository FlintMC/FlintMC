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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.mcapi.internal.resources.SkinCacheWebResource.Factory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;

@Singleton
@WebFileSystem("minecraft-skin-cache-head")
public class MinecraftHeadSkinCacheWebFileSystem implements WebFileSystemHandler {

  private final ResourceLocationProvider resourceLocationProvider;
  private final MinecraftSkinCacheDirectoryProvider minecraftSkinCacheDirectoryProvider;
  private final SkinCacheWebResource.Factory skinCacheWebResourceFactory;

  @Inject
  private MinecraftHeadSkinCacheWebFileSystem(
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
  public WebResource getFile(String path) {
    try {

      ResourceLocation resourceLocation = this.resourceLocationProvider.get(path);
      if (resourceLocation.exists()) {
        try (InputStream inputStream = resourceLocation.openInputStream()) {
          BufferedImage bufferedImage = ImageIO.read(inputStream);

          File file = new File(minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(),
              "heads/" + path);
          writeHead(bufferedImage, file);

          return skinCacheWebResourceFactory
              .create(minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), "heads/" + path);
        }
      }
      path = path.replaceFirst("skins/", "");
      path = path.substring(0, 2) + "/" + path;
      BufferedImage bufferedImage = ImageIO
          .read(new File(this.minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path));
      path = "heads/" + path;
      File file = new File(this.minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path);
      writeHead(bufferedImage, file);

      return skinCacheWebResourceFactory
          .create(minecraftSkinCacheDirectoryProvider.getSkinCacheDirectory(), path);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void writeHead(BufferedImage bufferedImage, File file) throws IOException {
    file.getParentFile().mkdirs();

    BufferedImage bi = new BufferedImage(32 * bufferedImage.getWidth(null),
        32 * bufferedImage.getHeight(null),
        BufferedImage.TYPE_INT_ARGB);

    Graphics2D grph = (Graphics2D) bi.getGraphics();
    grph.scale(32, 32);

    // everything drawn with grph from now on will get scaled.

    grph.drawImage(bufferedImage, 0, 0, null);
    grph.dispose();

    if (bi.getWidth() / bi.getHeight() == 1) {
      BufferedImage firstLayer = bi
          .getSubimage(bi.getWidth() / 8, bi.getHeight() / 8,
              bi.getWidth() / 8, bi.getHeight() / 8);

      BufferedImage secondLayer = bi
          .getSubimage(5 * bi.getWidth() / 8, bi.getHeight() / 8,
              bi.getWidth() / 8, bi.getHeight() / 8);

      Graphics2D graphics = (Graphics2D) firstLayer.getGraphics();
      graphics.drawImage(secondLayer, 0, 0, null);

      ImageIO.write(firstLayer, "PNG", file);
    } else if (bi.getWidth() / bi.getHeight() == 2) {
      BufferedImage firstLayer = bi
          .getSubimage(bi.getWidth() / 8, bi.getHeight() / 4,
              bi.getWidth() / 8, bi.getHeight() / 4);

      BufferedImage secondLayer = bi
          .getSubimage(5 * bi.getWidth() / 8, bi.getHeight() / 4,
              bi.getWidth() / 8, bi.getHeight() / 4);

      Graphics2D graphics = (Graphics2D) firstLayer.getGraphics();
      graphics.drawImage(secondLayer, 0, 0, null);

      ImageIO.write(firstLayer, "PNG", file);
    } else {
      throw new IllegalStateException("Malformed texture");
    }
  }
}
