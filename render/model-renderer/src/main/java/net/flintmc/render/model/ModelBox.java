package net.flintmc.render.model;

public interface ModelBox<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware,
            T_RenderContext,
            ModelBox<T_RenderContextAware, T_RenderContext>,
            Object>>
    extends Renderable<
    T_RenderContextAware,
    T_RenderContext,
    ModelBox<T_RenderContextAware, T_RenderContext>,
    Object> {

  ModelBox<T_RenderContextAware, T_RenderContext> set(Property property, float value);

  ModelBox<T_RenderContextAware, T_RenderContext> setMode(Property property, Property.Mode mode);

  float get(Property property);

  Property.Mode getMode(Property property);

  enum Property {
    ROTATION_ANGLE_X,
    ROTATION_ANGLE_Y,
    ROTATION_ANGLE_Z,

    ROTATION_POINT_X,
    ROTATION_POINT_Y,
    ROTATION_POINT_Z;

    public enum Mode {
      ABSOLUTE,
      RELATIVE
    }
  }
}
