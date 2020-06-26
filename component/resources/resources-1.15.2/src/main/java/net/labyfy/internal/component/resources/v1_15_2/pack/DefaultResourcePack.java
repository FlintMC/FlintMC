package net.labyfy.internal.component.resources.v1_15_2.pack;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.pack.ResourcePack;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.resources.ResourcePackType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * 1.15.2 implementation of a labyfy resource pack
 */
@Implement(value = ResourceLocation.class, version = "1.15.2")
public class DefaultResourcePack implements ResourcePack {
  private final ClientResourcePackInfo info;

  /**
   * Constructs a new {@link DefaultResourcePack} based on the given info.
   *
   * @param info Information about the resource pack
   */
  DefaultResourcePack(ClientResourcePackInfo info) {
    this.info = info;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getNameSpaces() {
    return this.info.getResourcePack().getResourceNamespaces(ResourcePackType.CLIENT_RESOURCES);
  }

  /**
   * {@inheritDoc}
   */
  public InputStream getStream(ResourceLocation resourceLocation) throws IOException {
    return this.info
        .getResourcePack()
        .getResourceStream(ResourcePackType.CLIENT_RESOURCES, resourceLocation.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return info.getName();
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return info.getDescription().getFormattedText();
  }

  /**
   * {@inheritDoc}
   */
  public String getTitle() {
    return info.getTitle().getFormattedText();
  }
}
