package net.flintmc.render.minecraft.v1_15_2.image;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.render.minecraft.image.ImageRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

@Singleton
@Implement(value = ImageRenderer.class, version = "1.15.2")
public class VersionedImageRenderer implements ImageRenderer {

  private final Minecraft minecraft;

  @Inject
  private VersionedImageRenderer() {
    this.minecraft = Minecraft.getInstance();
  }

  /** {@inheritDoc} */
  @Override
  public void bindTexture(ResourceLocation location) {
    this.minecraft.getTextureManager().bindTexture(location.getHandle());
  }

  /** {@inheritDoc} */
  @Override
  public void drawFullImage(
      float screenX,
      float screenY,
      int zLevel,
      float sourceWidth,
      float sourceHeight,
      float displayWidth,
      float displayHeight,
      int r,
      int g,
      int b,
      int a) {
    this.drawPartImage(
        screenX,
        screenY,
        0F,
        0F,
        zLevel,
        sourceWidth,
        sourceHeight,
        sourceWidth,
        sourceHeight,
        displayWidth,
        displayHeight,
        r,
        g,
        b,
        a);
  }

  /** {@inheritDoc} */
  @Override
  public void drawPartImage(
      float screenX,
      float screenY,
      float sourceX,
      float sourceY,
      int zLevel,
      float sourceWidth,
      float sourceHeight,
      float fullImageWidth,
      float fullImageHeight,
      float displayWidth,
      float displayHeight,
      int r,
      int g,
      int b,
      int a) {
    // no scaling if not set
    if (displayHeight == -1) {
      displayHeight = sourceHeight;
    }
    if (displayWidth == -1) {
      displayWidth = sourceWidth;
    }

    boolean colored = r != -1 && g != -1 && b != -1 && a != -1;
    if (colored) {
      RenderSystem.color4f(r / 255F, g / 255F, b / 255F, a / 255F);
    }

    boolean scale = sourceHeight != displayHeight || sourceWidth != displayWidth;

    if (scale) {
      RenderSystem.scalef(displayWidth / sourceWidth, displayHeight / sourceHeight, 1F);
    }

    float xScaleFactor = scale ? sourceWidth / displayWidth : 1F;
    float yScaleFactor = scale ? sourceHeight / displayHeight : 1F;

    AbstractGui.blit(
        (int) (screenX * xScaleFactor),
        (int) (screenY * yScaleFactor),
        zLevel,
        sourceX,
        sourceY,
        (int) sourceWidth,
        (int) sourceHeight,
        (int) fullImageWidth,
        (int) fullImageHeight);

    if (scale) {
      RenderSystem.scalef(xScaleFactor, yScaleFactor, 1F);
    }

    if (colored) {
      RenderSystem.color4f(1F, 1F, 1F, 1F);
    }
  }
}
