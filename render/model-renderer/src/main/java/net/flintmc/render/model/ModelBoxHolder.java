package net.flintmc.render.model;

import net.flintmc.util.property.Property;

import java.util.HashSet;
import java.util.Set;

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

  Property<Set<ModelBox>, Void> MODEL_BOXES =
      Property.builder().<Set<ModelBox>>withValue().withDefaultValue(HashSet::new).build();

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

  Set<ModelBox> getBoxes();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidthPolicy(
      OverridePolicy overridePolicy);

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeightPolicy(
      OverridePolicy overridePolicy);

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setModelBoxes(Set<ModelBox> modelBoxes);

  float getRotationPointX();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointX(float value);

  float getRotationPointY();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointY(float value);

  float getRotationPointZ();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointZ(float value);

  float getRotationAngleX();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleX(float value);

  float getRotationAngleY();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleY(float value);

  float getRotationAngleZ();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleZ(float value);

  boolean isMirror();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirror(boolean mirror);

  boolean isShowModel();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModel(boolean showModel);

  int getTextureOffsetX();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetX(int textureOffsetX);

  int getTextureOffsetY();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetY(int textureOffsetY);

  float getTextureWidth();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureWidth(float textureWidth);

  float getTextureHeight();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureHeight(float textureHeight);

  RotationMode getRotationPointXMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointXMode(RotationMode mode);

  RotationMode getRotationPointYMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointYMode(RotationMode mode);

  RotationMode getRotationPointZMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationPointZMode(RotationMode mode);

  RotationMode getRotationAngleXMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleXMode(RotationMode mode);

  RotationMode getRotationAngleYMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleYMode(RotationMode mode);

  RotationMode getRotationAngleZMode();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setRotationAngleZMode(RotationMode mode);

  OverridePolicy getMirrorOverridePolicy();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setMirrorOverridePolicy(
      OverridePolicy overridePolicy);

  OverridePolicy getShowModelOverridePolicy();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setShowModelOverridePolicy(
      OverridePolicy overridePolicy);

  OverridePolicy getTextureOffsetXOverridePolicy();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetXOverridePolicy(
      OverridePolicy overridePolicy);

  OverridePolicy getTextureOffsetYOverridePolicy();

  ModelBoxHolder<T_RenderContextAware, T_RenderContext> setTextureOffsetYOverridePolicy(
      OverridePolicy overridePolicy);

  OverridePolicy getTextureWidthOverridePolicy();

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
