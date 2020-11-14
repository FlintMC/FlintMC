package net.flintmc.render.model.internal;

import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;

import java.util.HashMap;
import java.util.Map;

public class DefaultModelBox<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware,
            T_RenderContext,
            ModelBox<T_RenderContextAware, T_RenderContext>,
            Object>,
    T_Renderable extends
        DefaultModelBox<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Target>
    extends DefaultRenderable<
    T_RenderContextAware,
    T_RenderContext,
    ModelBox<T_RenderContextAware, T_RenderContext>,
    Object>
    implements ModelBox<T_RenderContextAware, T_RenderContext> {

  private Map<Property, Float> propertyValues = new HashMap<>();
  private Map<Property, Property.Mode> propertyModes = new HashMap<>();

  protected DefaultModelBox(T_RenderContext renderContext, Object target) {
    super(renderContext, target);
  }

  public T_Renderable set(Property property, float value) {
    this.propertyValues.put(property, value);
    return (T_Renderable) this;
  }

  public float get(Property property) {
    return this.propertyValues.getOrDefault(property, 0f);
  }

  public Property.Mode getMode(Property property) {
    return this.propertyModes.getOrDefault(property, Property.Mode.RELATIVE);
  }

  public T_Renderable setMode(Property property, Property.Mode mode) {
    this.propertyModes.put(property, mode);
    return (T_Renderable) this;
  }
}
