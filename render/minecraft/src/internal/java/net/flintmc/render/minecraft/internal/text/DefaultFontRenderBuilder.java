package net.flintmc.render.minecraft.internal.text;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.minecraft.text.raw.FontRenderBuilder;
import net.flintmc.render.minecraft.text.raw.FontRenderer;
import net.flintmc.render.minecraft.text.raw.StringAlignment;

@Singleton
@Implement(FontRenderBuilder.class)
public class DefaultFontRenderBuilder implements FontRenderBuilder {

  private static final int WHITE = 16777215; // r = 255; g = 255; b = 255; a = 255

  private final FontRenderer renderer;
  private float x;
  private float y;
  private String text;
  private int rgba;
  private StringAlignment alignment;
  private int maxLineLength;
  private boolean shadow;
  private float xFactor;
  private float yFactor;

  @Inject
  private DefaultFontRenderBuilder(FontRenderer renderer) {
    this.renderer = renderer;
    this.reset();
  }

  private void validate() {
    Preconditions.checkNotNull(this.text, "Text not set/set to null");
    Preconditions.checkArgument(!this.text.isEmpty(), "Text cannot be empty");
    Preconditions.checkNotNull(this.alignment, "Alignment cannot be null");
    Preconditions.checkArgument(
        this.maxLineLength >= 0, "MaxLineLength cannot be < 0 (only 0 disables splitting)");
  }

  private void reset() {
    this.x = -1;
    this.y = -1;
    this.text = null;
    this.rgba = WHITE;
    this.alignment = StringAlignment.LEFT;
    this.maxLineLength = 0;
    this.shadow = true;
    this.xFactor = 1;
    this.yFactor = 1;
  }

  @Override
  public FontRenderBuilder at(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  @Override
  public FontRenderBuilder text(String text) {
    this.text = text;
    return this;
  }

  @Override
  public FontRenderBuilder scale(float factor) {
    return this.scale(factor, factor);
  }

  @Override
  public FontRenderBuilder scale(float xFactor, float yFactor) {
    this.xFactor = xFactor;
    this.yFactor = yFactor;
    return this;
  }

  @Override
  public FontRenderBuilder color(int rgba) {
    this.rgba = rgba;
    return this;
  }

  @Override
  public FontRenderBuilder color(int r, int g, int b) {
    return this.color(r, g, b, 255);
  }

  @Override
  public FontRenderBuilder color(int r, int g, int b, int a) {
    return this.color(((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
  }

  @Override
  public FontRenderBuilder align(StringAlignment alignment) {
    this.alignment = alignment;
    return this;
  }

  @Override
  public FontRenderBuilder useMultipleLines(int maxLineLength) {
    this.maxLineLength = maxLineLength;
    return this;
  }

  @Override
  public FontRenderBuilder useMultipleLines() {
    return this.useMultipleLines(Integer.MAX_VALUE);
  }

  @Override
  public FontRenderBuilder disableShadow() {
    this.shadow = false;
    return this;
  }

  @Override
  public void draw() {
    this.validate();

    this.renderer.drawString(
        this.x,
        this.y,
        this.text,
        this.rgba,
        this.alignment,
        this.maxLineLength,
        this.shadow,
        this.xFactor,
        this.yFactor);

    this.reset();
  }
}
