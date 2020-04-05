package net.labyfy.component.resources.v1_15_1.pack;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.pack.ResourcePack;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.resources.ResourcePackType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class LabyResourcePack implements ResourcePack {

  private final ResourceLocationProvider resourceLocationProvider;
  private final ClientResourcePackInfo info;

  @AssistedInject
  private LabyResourcePack(
      ResourceLocationProvider resourceLocationProvider, @Assisted ClientResourcePackInfo info) {
    this.resourceLocationProvider = resourceLocationProvider;
    this.info = info;
  }

  public Collection<String> getNameSpaces() {
    return this.info.getResourcePack().getResourceNamespaces(ResourcePackType.CLIENT_RESOURCES);
  }

  public InputStream getStream(ResourceLocation resourceLocation) {
    try {
      return this.info
          .getResourcePack()
          .getResourceStream(ResourcePackType.CLIENT_RESOURCES, resourceLocation.getHandle());
    } catch (IOException e) {
    }
    return null;
  }

  public String getName() {
    return info.getName();
  }

  public String getDescription() {
    return info.getDescription().getFormattedText();
  }

  public String getTitle() {
    return info.getTitle().getFormattedText();
  }

  @AssistedFactory(LabyResourcePack.class)
  public interface Factory {
    LabyResourcePack create(ClientResourcePackInfo info);
  }
}
