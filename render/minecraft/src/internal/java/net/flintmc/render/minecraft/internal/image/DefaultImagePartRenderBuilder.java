package net.flintmc.render.minecraft.internal.image;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.minecraft.image.ImagePartRenderBuilder;
import net.flintmc.render.minecraft.image.ImageRenderer;

@Singleton
@Implement(ImagePartRenderBuilder.class)
public class DefaultImagePartRenderBuilder extends DefaultImageFullRenderBuilder
    implements ImagePartRenderBuilder {

  protected float sourceX;
  protected float sourceY;
  protected float sourceWidth;
  protected float sourceHeight;

  @Inject
  protected DefaultImagePartRenderBuilder(ImageRenderer renderer) {
    super(renderer);
    this.reset();
  }

  @Override
  protected void reset() {
    super.reset();

    this.sourceX = -1;
    this.sourceY = -1;
    this.sourceWidth = -1;
    this.sourceHeight = -1;
  }

  @Override
  protected void validate() {
    super.validate();

    Preconditions.checkArgument(
        this.sourceWidth > 0 && this.sourceHeight > 0, "Source size not set/set to something <= 0");
  }

  @Override
  public ImagePartRenderBuilder sourcePosition(float x, float y) {
    this.sourceX = x;
    this.sourceY = y;
    return this;
  }

  @Override
  public ImagePartRenderBuilder sourceSize(float width, float height) {
    this.sourceWidth = width;
    this.sourceHeight = height;
    return this;
  }

  @Override
  public void draw() {
    this.validate();

    this.renderer.drawPartImage(
        this.x,
        this.y,
        this.sourceX,
        this.sourceY,
        this.zLevel,
        this.matrix,
        this.sourceWidth,
        this.sourceHeight,
        this.imageWidth,
        this.imageHeight,
        this.displayWidth,
        this.displayHeight,
        this.r,
        this.g,
        this.b,
        this.a);

    this.reset();
  }
}
