package net.flintmc.render.model;

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

  FloatProperty<RotationProperty.Mode> ROTATION_ANGLE_X = new RotationProperty();
  FloatProperty<RotationProperty.Mode> ROTATION_ANGLE_Y = new RotationProperty();
  FloatProperty<RotationProperty.Mode> ROTATION_ANGLE_Z = new RotationProperty();

  FloatProperty<RotationProperty.Mode> ROTATION_POINT_X = new RotationProperty();
  FloatProperty<RotationProperty.Mode> ROTATION_POINT_Y = new RotationProperty();
  FloatProperty<RotationProperty.Mode> ROTATION_POINT_Z = new RotationProperty();

  IntProperty<Boolean> TEXTURE_OFFSET_X = new TextureOffsetProperty();
  IntProperty<Boolean> TEXTURE_OFFSET_Y = new TextureOffsetProperty();

  FloatProperty<Boolean> TEXTURE_WIDTH = new TextureSizeProperty();
  FloatProperty<Boolean> TEXTURE_HEIGHT = new TextureSizeProperty();

  BooleanProperty<Boolean> MIRROR =
      new BooleanProperty<Boolean>() {
        public boolean validateValue(Boolean value) {
          return value != null;
        }

        public boolean validateMeta(Boolean value) {
          return value != null;
        }

        public Boolean getDefaultMeta() {
          return false;
        }
      };

  SetProperty<ModelBox, Void> MODEL_BOXES =
      new SetProperty<ModelBox, Void>() {
        public boolean validateValue(Set<ModelBox> modelBoxes) {
          return modelBoxes != null;
        }

        public boolean validateMeta(Void value) {
          return false;
        }

        public Void getDefaultMeta() {
          return null;
        }
      };

  BooleanProperty<Boolean> SHOW_MODEL =
      new BooleanProperty<Boolean>() {
        public boolean validateValue(Boolean value) {
          return value != null;
        }

        public boolean validateMeta(Boolean value) {
          return value != null;
        }

        public Boolean getDefaultMeta() {
          return false;
        }

        public Boolean getDefaultValue() {
          return true;
        }
      };

  class TextureSizeProperty implements FloatProperty<Boolean> {
    public boolean validateValue(Float value) {
      return value != null;
    }

    public boolean validateMeta(Boolean value) {
      return value != null;
    }

    public Boolean getDefaultMeta() {
      return false;
    }
  }

  class TextureOffsetProperty implements IntProperty<Boolean> {
    public boolean validateValue(Integer integer) {
      return integer != null;
    }

    public boolean validateMeta(Boolean value) {
      return value != null;
    }

    public Boolean getDefaultMeta() {
      return false;
    }
  }

  class RotationProperty implements FloatProperty<RotationProperty.Mode> {
    public boolean validateValue(Float value) {
      return value != null;
    }

    public boolean validateMeta(RotationProperty.Mode mode) {
      return mode != null;
    }

    public RotationProperty.Mode getDefaultMeta() {
      return RotationProperty.Mode.RELATIVE;
    }

    public enum Mode {
      ABSOLUTE,
      RELATIVE
    }
  }
}
