package net.labyfy.component.gui.texture;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.WrapResourceLocation;
import net.labyfy.component.resources.WrappedResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class Texture extends WrappedResourceLocation {

  private final int id;

  @AssistedInject
  private Texture(@Assisted ResourceLocation resourceLocation) {
    super(resourceLocation);
    this.id = GL11.glGenTextures();
  }

  public int getId() {
    return id;
  }

  @AssistedFactory(Texture.class)
  public interface Factory {
    @WrapResourceLocation
    Texture wrap(ResourceLocation resourceLocation);
  }

}
