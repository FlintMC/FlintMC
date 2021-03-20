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

package net.flintmc.render.model;

import java.awt.*;
import net.flintmc.util.property.Property;

/**
 * Represents the well known vanilla MinecraftRenderer.
 *
 * @param <T_RenderContextAware> the
 * @param <T_RenderContext>
 */
public interface ModelBoxHolder<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware,
            T_RenderContext,
            ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
            ?,
            Object>>
    extends Renderable<
    T_RenderContextAware,
    T_RenderContext,
    ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
    Object> {

  Property<Float, RotationMode> ROTATION_ANGLE_X =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Float, RotationMode> ROTATION_ANGLE_Y =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Float, RotationMode> ROTATION_ANGLE_Z =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Float, RotationMode> ROTATION_POINT_X =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Float, RotationMode> ROTATION_POINT_Y =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Float, RotationMode> ROTATION_POINT_Z =
      Property.builder()
          .<Float>withValue()
          .<RotationMode>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(RotationMode.ABSOLUTE)
          .build();
  Property<Integer, OverridePolicy> TEXTURE_OFFSET_X =
      Property.builder()
          .<Integer>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(0)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Integer, OverridePolicy> TEXTURE_OFFSET_Y =
      Property.builder()
          .<Integer>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(0)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Float, OverridePolicy> TEXTURE_WIDTH =
      Property.builder()
          .<Float>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Float, OverridePolicy> TEXTURE_HEIGHT =
      Property.builder()
          .<Float>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(0f)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Boolean, OverridePolicy> SHOW_MODEL =
      Property.builder()
          .<Boolean>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(true)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Boolean, OverridePolicy> MIRROR =
      Property.builder()
          .<Boolean>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(false)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();
  Property<Color, OverridePolicy> COLOR =
      Property.builder()
          .<Color>withValue()
          .<OverridePolicy>withMeta()
          .withDefaultValue(Color.BLACK)
          .withDefaultMeta(OverridePolicy.INACTIVE)
          .build();

  /**
   * Set the texture width override policy.
   *
   * @param overridePolicy the override policy to set
   * @return this
   * @see OverridePolicy
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidthPolicy(
      OverridePolicy overridePolicy);

  /**
   * Set the texture height override policy.
   *
   * @param overridePolicy the override policy to set
   * @return this
   * @see OverridePolicy
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeightPolicy(
      OverridePolicy overridePolicy);

  /**
   * @return the vertex color
   */
  Color getColor();

  /**
   * Set the vertex color.
   *
   * @param color the color to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setColor(Color color);

  /**
   * @return the x translation
   */
  float getTranslationX();

  /**
   * Set the x translation.
   *
   * @param value the x translation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationX(float value);

  /**
   * @return the y translation
   */
  float getTranslationY();

  /**
   * Set the y translation.
   *
   * @param value the y translation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationY(float value);

  /**
   * @return the z translation
   */
  float getTranslationZ();

  /**
   * Set the z translation.
   *
   * @param value the z translation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZ(float value);

  /**
   * @return the x rotation
   */
  float getRotationX();

  /**
   * Set the x rotation.
   *
   * @param value the x rotation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationX(float value);

  /**
   * @return the y rotation
   */
  float getRotationY();

  /**
   * Set the y rotation.
   *
   * @param value the y rotation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationY(float value);

  /**
   * @return the z rotation
   */
  float getRotationZ();

  /**
   * Set the z rotation.
   *
   * @param value the z rotation to set
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZ(float value);

  /**
   * @return if the rendering should be mirrored at the x axis.
   */
  boolean isMirror();

  /**
   * Sets if the rendering should be mirrored at the x axis.
   *
   * @param mirror new mirror value
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirror(boolean mirror);

  /**
   * @return if the model should be rendered
   */
  boolean isShowModel();

  /**
   * Sets if the model should be rendered.
   *
   * @param showModel new value for showModel
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModel(boolean showModel);

  /**
   * @return the texture x-offset
   */
  int getTextureOffsetX();

  /**
   * Sets the texture x-offset
   *
   * @param textureOffsetX the new x-offset
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetX(int textureOffsetX);

  /**
   * @return the texture y-offset
   */
  int getTextureOffsetY();

  /**
   * Sets the texture y-offset
   *
   * @param textureOffsetY the new y-offset
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetY(int textureOffsetY);

  /**
   * @return the texture width
   */
  float getTextureWidth();

  /**
   * Sets the texture width.
   *
   * @param textureWidth the new texture width.
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidth(float textureWidth);

  /**
   * @return the texture height
   */
  float getTextureHeight();

  /**
   * Sets the texture height.
   *
   * @param textureHeight the new texture height.
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeight(float textureHeight);

  /**
   * @return the x translation mode
   */
  RotationMode getTranslationXMode();

  /**
   * Sets the x translation mode.
   *
   * @param mode the new x translation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationXMode(RotationMode mode);

  /**
   * @return the y translation mode
   */
  RotationMode getTranslationYMode();

  /**
   * Sets the y translation mode.
   *
   * @param mode the new y translation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationYMode(RotationMode mode);

  /**
   * @return the z translation mode
   */
  RotationMode getTranslationZMode();

  /**
   * Sets the z translation mode.
   *
   * @param mode the new z translation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTranslationZMode(RotationMode mode);

  /**
   * @return the x rotation mode
   */
  RotationMode getRotationXMode();

  /**
   * Sets the x rotation mode
   *
   * @param mode the new x rotation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationXMode(RotationMode mode);

  /**
   * @return the y rotation mode
   */
  RotationMode getRotationYMode();

  /**
   * Sets the y rotation mode
   *
   * @param mode the new y rotation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationYMode(RotationMode mode);

  /**
   * @return the z rotation mode
   */
  RotationMode getRotationZMode();

  /**
   * Sets the z rotation mode
   *
   * @param mode the new z rotation mode
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationZMode(RotationMode mode);

  /**
   * @return the mirror override policy
   */
  OverridePolicy getMirrorOverridePolicy();

  /**
   * Set the mirror override policy
   *
   * @param overridePolicy the new mirror override policy
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirrorOverridePolicy(
      OverridePolicy overridePolicy);

  /**
   * @return the showModel override policy
   */
  OverridePolicy getShowModelOverridePolicy();

  /**
   * Set the showModel override policy
   *
   * @param overridePolicy the new showModel override policy
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModelOverridePolicy(
      OverridePolicy overridePolicy);

  /**
   * @return the texture x offset override policy
   */
  OverridePolicy getTextureOffsetXOverridePolicy();

  /**
   * Set the texture x offset override policy
   *
   * @param overridePolicy the new texture x offset override policy
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetXOverridePolicy(
      OverridePolicy overridePolicy);

  /**
   * @return the texture y offset override policy
   */
  OverridePolicy getTextureOffsetYOverridePolicy();

  /**
   * Set the texture y offset override policy
   *
   * @param overridePolicy the new texture y offset override policy
   * @return this
   */
  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetYOverridePolicy(
      OverridePolicy overridePolicy);

  /**
   * @return the texture width override policy
   */
  OverridePolicy getTextureWidthOverridePolicy();

  /**
   * @return the texture height override policy
   */
  OverridePolicy getTextureHeightOverridePolicy();


  enum OverridePolicy {
    ACTIVE,
    INACTIVE

  }

  enum RotationMode {
    ABSOLUTE,
    RELATIVE
  }
}
