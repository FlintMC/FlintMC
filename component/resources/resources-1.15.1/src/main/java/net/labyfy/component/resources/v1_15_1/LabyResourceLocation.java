package net.labyfy.component.resources.v1_15_1;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;

@Implement(value = ResourceLocation.class, version = "1.15.1")
public class LabyResourceLocation extends net.minecraft.util.ResourceLocation
    implements ResourceLocation {

  @AssistedInject
  private LabyResourceLocation(@Assisted("nameSpace") String nameSpace) {
    super(nameSpace);
  }

  @AssistedInject
  private LabyResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path) {
    super(nameSpace, path);
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
}
