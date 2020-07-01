package net.labyfy.internal.component.gui.juklearmc.v1_15_2.backend;

import net.janrupf.juklear.Juklear;
import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.backend.JuklearBackend;
import net.janrupf.juklear.drawing.JuklearAntialiasing;
import net.janrupf.juklear.drawing.JuklearDrawNullTexture;
import net.janrupf.juklear.ffi.CAccessibleObject;
import net.janrupf.juklear.font.JuklearFontAtlasFormat;
import net.janrupf.juklear.image.JuklearImageFormat;
import net.janrupf.juklear.image.JuklearJavaImage;
import net.janrupf.juklear.math.JuklearVec2;

import java.nio.ByteBuffer;

public class MinecraftJuklearBackend implements JuklearBackend {
  private Juklear juklear;
  private MinecraftJuklearOpenGLDevice device;

  @Override
  public void init(Juklear juklear) {
    this.juklear = juklear;
    this.device = new MinecraftJuklearOpenGLDevice(juklear);
  }

  @Override
  public void draw(
      JuklearContext context, int width, int height, JuklearVec2 scale, JuklearAntialiasing antialiasing) {
    device.draw(context, width, height, scale, antialiasing);
  }

  @Override
  public JuklearFontAtlasFormat fontAtlasFormat() {
    return JuklearFontAtlasFormat.RGBA32;
  }

  @Override
  public JuklearJavaImage uploadFontAtlas(CAccessibleObject<?> image, int width, int height) {
    return device.uploadFontAtlas(image, width, height);
  }

  @Override
  public void setNullTexture(JuklearDrawNullTexture texture) {
    device.setNullTexture(texture);
  }

  @Override
  public CAccessibleObject<?> createImage(JuklearImageFormat format, ByteBuffer data, int width, int height) {
    return new MinecraftJuklearOpenGLImage(device.getPreFrameTasks(), device.uploadTexture(format, data, width, height));
  }
}
