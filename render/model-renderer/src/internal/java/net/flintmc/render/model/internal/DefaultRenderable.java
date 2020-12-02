package net.flintmc.render.model.internal;

import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;
import net.flintmc.render.model.Renderable;
import net.flintmc.util.property.PropertyContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class DefaultRenderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, ?, T_RenderTarget>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget>
    implements Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget> {

  private final T_RenderContext renderContext;
  private final T_RenderTarget meta;
  private final Collection<Consumer<T_Renderable>> renderPreparations = new HashSet<>();
  private final Collection<Consumer<T_Renderable>> propertyPreparations = new HashSet<>();
  private final Collection<Consumer<T_Renderable>> renderCleanups = new HashSet<>();
  private PropertyContext<T_Renderable> propertyContext;

  private Consumer<T_Renderable> propertyHandler = renderable -> {
  };

  protected DefaultRenderable(T_RenderContext renderContext, T_RenderTarget meta) {
    this.renderContext = renderContext;
    this.meta = meta;
  }

  @SuppressWarnings("unchecked")
  public PropertyContext<T_Renderable> getPropertyContext() {
    if (this.propertyContext == null)
      this.propertyContext =
          InjectionHolder.getInjectedInstance(PropertyContext.Factory.class)
              .create((T_Renderable) this);
    return this.propertyContext;
  }

  @SuppressWarnings("unchecked")
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
  public T_Renderable callPropertyPreparations() {
    for (Consumer<T_Renderable> propertyPreparation : this.propertyPreparations) {
      propertyPreparation.accept((T_Renderable) this);
    }
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public T_Renderable callRenderCleanup() {
    for (Consumer<T_Renderable> renderCleanup : this.renderCleanups) {
      renderCleanup.accept((T_Renderable) this);
    }
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
  public T_Renderable addPropertyPreparation(Consumer<T_Renderable> consumer) {
    this.propertyPreparations.add(consumer);
    return (T_Renderable) this;
  }

  @SuppressWarnings("unchecked")
  public T_Renderable addRenderCleanup(Consumer<T_Renderable> consumer) {
    this.renderCleanups.add(consumer);
    return (T_Renderable) this;
  }

  public T_RenderTarget getTarget() {
    return this.meta;
  }
}
