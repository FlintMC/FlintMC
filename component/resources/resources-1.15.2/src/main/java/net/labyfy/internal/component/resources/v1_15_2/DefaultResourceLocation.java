package net.labyfy.internal.component.resources.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1.15.2 implementation of a minecraft resource location.
 */
@Implement(value = ResourceLocation.class, version = "1.15.2")
public class DefaultResourceLocation extends net.minecraft.util.ResourceLocation implements ResourceLocation {
  @AssistedInject
  private DefaultResourceLocation(@Assisted("fullPath") String fullPath) {
    super(fullPath);
  }

  @AssistedInject
  private DefaultResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path) {
    super(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  public InputStream openInputStream() throws IOException {
    return Minecraft.getInstance().getResourceManager().getResource(this).getInputStream();
  }
}
