package net.flintmc.render.model.internal;

import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;
import net.flintmc.render.model.Renderable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultRenderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
    T_RenderMeta>
    implements Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta> {

  private final T_RenderContext renderContext;
  private final T_RenderMeta meta;
  private final Collection<Consumer<T_Renderable>> renderPreparations = new HashSet<>();
  private final Map<Property<?, ?>, Object> propertyValues = new HashMap<>();
  private final Map<Property<?, ?>, Object> propertyMeta = new HashMap<>();
  private Consumer<T_Renderable> propertyHandler = renderable -> {
  };

  protected DefaultRenderable(T_RenderContext renderContext, T_RenderMeta meta) {
    this.renderContext = renderContext;
    this.meta = meta;
  }

  public T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer) {
    this.renderPreparations.add(consumer);
    return (T_Renderable) this;
  }

  public T_RenderContext getContext() {
    return this.renderContext;
  }

  @SuppressWarnings("unchecked")
  public T_Renderable setPropertyHandler(Consumer<T_Renderable> propertyHandler) {
    this.propertyHandler = propertyHandler;
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public T_Renderable callPropertyHandler() {
    this.propertyHandler.accept((T_Renderable) this);
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public T_Renderable callRenderPreparations() {
    for (Consumer<T_Renderable> preparation : this.renderPreparations) {
      preparation.accept((T_Renderable) this);
    }
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public <T_PropertyType, T_PropertyMeta> T_Renderable setPropertyValue(
      Property<T_PropertyType, T_PropertyMeta> property, T_PropertyType propertyValue) {
    if (!property.validateValue(propertyValue))
      throw new IllegalArgumentException("provided property value is invalid.");
    this.propertyValues.put(property, propertyValue);
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public <T_PropertyType, T_PropertyMeta> T_Renderable setPropertyMeta(
      Property<T_PropertyType, T_PropertyMeta> property, T_PropertyMeta propertyMeta) {
    if (!property.validateMeta(propertyMeta))
      throw new IllegalArgumentException("provided property meta is invalid.");
    this.propertyMeta.put(property, propertyMeta);
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public <T_PropertyType, T_PropertyMeta> T_PropertyType getPropertyValue(
      Property<T_PropertyType, T_PropertyMeta> property) {
    return (T_PropertyType) this.propertyValues.getOrDefault(property, property.getDefaultValue());
  }

  @SuppressWarnings("unchecked")
  public <T_PropertyType, T_PropertyMeta> T_PropertyMeta getPropertyMeta(
      Property<T_PropertyType, T_PropertyMeta> property) {
    return (T_PropertyMeta) this.propertyMeta.getOrDefault(property, property.getDefaultMeta());
  }

  public T_RenderMeta getMeta() {
    return this.meta;
  }
}
