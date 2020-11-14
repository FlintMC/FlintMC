package net.flintmc.render.model.internal;

import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;
import net.flintmc.render.model.Renderable;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class DefaultRenderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Target>
    implements Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target> {

  private final T_RenderContext renderContext;
  private final T_Target owner;
  private final Collection<Consumer<T_Renderable>> renderPreparations = new HashSet<>();
  private Consumer<T_Renderable> propertyHandler = renderable -> {
  };

  protected DefaultRenderable(T_RenderContext renderContext, T_Target owner) {
    this.renderContext = renderContext;
    this.owner = owner;
  }

  public T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer) {
    this.renderPreparations.add(consumer);
    return (T_Renderable) this;
  }

  public T_RenderContext getContext() {
    return this.renderContext;
  }

  public T_Renderable setPropertyHandler(Consumer<T_Renderable> propertyHandler) {
    this.propertyHandler = propertyHandler;
    return (T_Renderable) this;
  }

  public T_Renderable callPropertyHandler() {
    this.propertyHandler.accept((T_Renderable) this);
    return (T_Renderable) this;
  }

  public T_Renderable callRenderPreparations() {
    for (Consumer<T_Renderable> preparation : this.renderPreparations) {
      preparation.accept((T_Renderable) this);
    }
    return (T_Renderable) this;
  }

  public T_Target getTarget() {
    return this.owner;
  }
}
