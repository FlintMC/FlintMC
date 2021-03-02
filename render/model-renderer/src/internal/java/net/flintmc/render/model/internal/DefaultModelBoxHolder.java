/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.model.internal;

import java.awt.*;
import java.util.Set;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;

/**
 * {@inheritDoc}
 */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ModelBox> getBoxes() {
    return this.getPropertyContext().getPropertyValue(ModelBoxHolder.MODEL_BOXES);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationX(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_X, value);
  }

  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> addMatrixHandler(){
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationY(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_Y, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZ(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_ANGLE_Z, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationXMode(RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_X, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationYMode(RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_Y, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZMode(RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_ANGLE_Z, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationX(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_X, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationY(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_Y, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZ(float value) {
    return this.getPropertyContext().setPropertyValue(ROTATION_POINT_Z, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationXMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_X, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationYMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_Y, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZMode(
      RotationMode mode) {
    return this.getPropertyContext().setPropertyMeta(ROTATION_POINT_Z, mode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTranslationX() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTranslationY() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTranslationZ() {
    return this.getPropertyContext().getPropertyValue(ROTATION_POINT_Z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotationX() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotationY() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotationZ() {
    return this.getPropertyContext().getPropertyValue(ROTATION_ANGLE_Z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getTranslationXMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getTranslationYMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getTranslationZMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_POINT_Z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getRotationXMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getRotationYMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RotationMode getRotationZMode() {
    return this.getPropertyContext().getPropertyMeta(ROTATION_ANGLE_Z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirror(boolean mirror) {
    return this.getPropertyContext().setPropertyValue(MIRROR, mirror);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModel(boolean showModel) {
    return this.getPropertyContext().setPropertyValue(SHOW_MODEL, showModel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirrorOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(MIRROR, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModelOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(SHOW_MODEL, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetX(
      int textureOffsetX) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_OFFSET_X, textureOffsetX);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetY(
      int textureOffsetY) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_OFFSET_X, textureOffsetY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidth(float textureWidth) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_WIDTH, textureWidth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeight(
      float textureHeight) {
    return this.getPropertyContext().setPropertyValue(TEXTURE_WIDTH, textureHeight);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetXOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_OFFSET_X, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetYOverridePolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_OFFSET_Y, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidthPolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_WIDTH, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeightPolicy(
      OverridePolicy overridePolicy) {
    return this.getPropertyContext().setPropertyMeta(TEXTURE_HEIGHT, overridePolicy);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setModelBoxes(
      Set<ModelBox> modelBoxes) {
    return this.getPropertyContext().setPropertyValue(MODEL_BOXES, modelBoxes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBoxHolder<T_RenderContextAware, T_RenderContext> setColor(Color color) {
    return this.getPropertyContext().setPropertyValue(COLOR, color);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Color getColor() {
    return this.getPropertyContext().getPropertyValue(COLOR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMirror() {
    return this.getPropertyContext().getPropertyValue(MIRROR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowModel() {
    return this.getPropertyContext().getPropertyValue(SHOW_MODEL);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTextureOffsetX() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_OFFSET_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTextureOffsetY() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_OFFSET_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTextureWidth() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_WIDTH);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTextureHeight() {
    return this.getPropertyContext().getPropertyValue(TEXTURE_HEIGHT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getMirrorOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(MIRROR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getShowModelOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(SHOW_MODEL);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getTextureOffsetXOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_OFFSET_X);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getTextureOffsetYOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_OFFSET_Y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getTextureWidthOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_WIDTH);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OverridePolicy getTextureHeightOverridePolicy() {
    return this.getPropertyContext().getPropertyMeta(TEXTURE_HEIGHT);
  }
}
