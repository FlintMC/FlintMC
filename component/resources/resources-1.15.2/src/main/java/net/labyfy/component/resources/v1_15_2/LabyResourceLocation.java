package net.labyfy.component.resources.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.labyfy.component.resources.WrappedResourceLocation;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;

@Implement(value = ResourceLocation.class, version = "1.15.2")
public class LabyResourceLocation extends net.minecraft.util.ResourceLocation
    implements ResourceLocation {

  private final ResourceLocationProvider resourceLocationProvider;

  @AssistedInject
  private LabyResourceLocation(@Assisted("nameSpace") String nameSpace, ResourceLocationProvider resourceLocationProvider) {
    super(nameSpace);
    this.resourceLocationProvider = resourceLocationProvider;
  }

  @AssistedInject
  private LabyResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path, ResourceLocationProvider resourceLocationProvider) {
    super(nameSpace, path);
    this.resourceLocationProvider = resourceLocationProvider;
  }

  /**
   * this method exists only to not enforce other implementations to extend {@link
   * net.minecraft.util.ResourceLocation} In this implementation this is instance of {@link
   * net.minecraft.util.ResourceLocation}
   */
  public <T> T getHandle() {
    return (T) this;
  }

  public InputStream openInputStream() {
    try {
      return Minecraft.getInstance().getResourceManager().getResource(this).getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public <T extends WrappedResourceLocation> T as(Class<T> clazz) {
    return this.resourceLocationProvider.wrap(this, clazz);
  }
}
