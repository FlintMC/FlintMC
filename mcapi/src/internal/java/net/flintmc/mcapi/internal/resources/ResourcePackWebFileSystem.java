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
