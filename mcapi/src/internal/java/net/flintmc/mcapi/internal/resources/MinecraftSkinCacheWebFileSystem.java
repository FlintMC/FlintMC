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
