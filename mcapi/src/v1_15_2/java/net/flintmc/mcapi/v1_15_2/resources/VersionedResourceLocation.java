package net.flintmc.mcapi.v1_15_2.resources;

import java.io.IOException;
import java.io.InputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

/** 1.15.2 implementation of a minecraft resource location. */
@Implement(value = ResourceLocation.class, version = "1.15.2")
public class VersionedResourceLocation extends net.minecraft.util.ResourceLocation
    implements ResourceLocation {
  @AssistedInject
  private VersionedResourceLocation(@Assisted("fullPath") String fullPath) {
    super(fullPath);
  }

  @AssistedInject
  private VersionedResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path) {
    super(nameSpace, path);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this;
  }

  /** {@inheritDoc} */
  public InputStream openInputStream() throws IOException {
    return Minecraft.getInstance().getResourceManager().getResource(this).getInputStream();
  }

  /** {@inheritDoc} */
  @Override
  public boolean exists() {
    return Minecraft.getInstance().getResourceManager().hasResource(this);
  }

  /**
   * Retrieves the resource location as a {@link String}.
   *
   * @return The resource location as a string.
   */
  @Override
  public String toString() {
    return this.namespace + ":" + this.path;
  }
}
