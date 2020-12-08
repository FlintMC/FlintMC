package net.flintmc.render.model.internal;

import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;

import java.awt.*;
import java.util.Set;

public class DefaultModelBoxHolder<
        T_RenderContextAware extends RenderContextAware<T_RenderContext>,
        T_RenderContext extends
            RenderContext<
                    T_RenderContextAware,
                    T_RenderContext,
                    ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
                    ?,
                    Object>,
        T_Renderable extends
            DefaultModelBoxHolder<
                    T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
        T_RenderTarget>
    extends DefaultRenderable<
        T_RenderContextAware,
        T_RenderContext,
        ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
        Object>
    implements ModelBoxHolder<T_RenderContextAware, T_RenderContext> {

  protected DefaultModelBoxHolder(T_RenderContext renderContext, Object meta) {
    super(renderContext, meta);
  }

  public Set<ModelBox> getBoxes() {
    return this.getPropertyContext().getPropertyValue(ModelBoxHolder.MODEL_BOXES);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationX(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_X, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationY(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_Y, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZ(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_Z, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationXMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_X, mode);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationYMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_Y, mode);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_Z, mode);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationX(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_X, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationY(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_Y, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZ(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_Z, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationXMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_X, mode);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationYMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_Y, mode);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_Z, mode);
  }

  public float getTranslationX() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_X);
  }

  public float getTranslationY() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_Y);
  }

  public float getTranslationZ() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_Z);
  }

  public float getRotationX() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_X);
  }

  public float getRotationY() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_Y);
  }

  public float getRotationZ() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_Z);
  }

  public RotationMode getTranslationXMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_X);
  }

  public RotationMode getTranslationYMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_Y);
  }

  public RotationMode getTranslationZMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_Z);
  }

  public RotationMode getRotationXMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_X);
  }

  public RotationMode getRotationYMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_Y);
  }

  public RotationMode getRotationZMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_Z);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirror(boolean mirror) {
    return this.getPropertyContext().setPropertyValue(MIRROR, mirror);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModel(boolean showModel) {
    return this.getPropertyContext().setPropertyValue(SHOW_MODEL, showModel);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirrorOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(MIRROR, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModelOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(SHOW_MODEL, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetX(
      int textureOffsetX) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_OFFSET_X, textureOffsetX);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetY(
      int textureOffsetY) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_OFFSET_X, textureOffsetY);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidth(float textureWidth) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_WIDTH, textureWidth);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeight(
      float textureHeight) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_WIDTH, textureHeight);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetXOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_OFFSET_X, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetYOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_OFFSET_Y, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidthPolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_WIDTH, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeightPolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_HEIGHT, overridePolicy);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setModelBoxes(
      Set<ModelBox> modelBoxes) {
    return this.getPropertyContext().setPropertyValue(MODEL_BOXES, modelBoxes);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setColor(Color color) {
    return this.getPropertyContext().setPropertyValue(COLOR, color);
  }

  public Color getColor() {
    return this.getPropertyContext().getPropertyValue(COLOR);
  }

  public boolean isMirror() {
    return this.getPropertyContext().getPropertyValue(MIRROR);
  }

  public boolean isShowModel() {
    return this.getPropertyContext().getPropertyValue(SHOW_MODEL);
  }

  public int getTextureOffsetX() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_OFFSET_X);
  }

  public int getTextureOffsetY() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_OFFSET_Y);
  }

  public float getTextureWidth() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_WIDTH);
  }

  public float getTextureHeight() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_HEIGHT);
  }

  public OverridePolicy getMirrorOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(MIRROR);
  }

  public OverridePolicy getShowModelOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(SHOW_MODEL);
  }

  public OverridePolicy getTextureOffsetXOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_OFFSET_X);
  }

  public OverridePolicy getTextureOffsetYOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_OFFSET_Y);
  }

  public OverridePolicy getTextureWidthOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_WIDTH);
  }

  public OverridePolicy getTextureHeightOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_HEIGHT);
  }
}
