package net.flintmc.render.model.internal;

import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderableRepository;
import net.flintmc.render.model.Renderable;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultRenderable<R extends Renderable<R, T>, T> implements Renderable<R, T> {


  private final Supplier<RenderContext<?, R>> renderContext;
  private final T target;
  private final Collection<Consumer<R>> beforeRenders = new HashSet<>();
  private final Collection<Consumer<R>> afterRenders = new HashSet<>();
  private Consumer<R> rendererNotSetAction = renderable -> {
  };

  protected DefaultRenderable(Supplier<RenderContext<?, R>> renderContext, RenderableRepository repository, T target) {
    this.renderContext = renderContext;
    this.target = target;
    repository.register(this);
  }

  public R addBeforeRenderHook(Consumer<R> consumer) {
    this.beforeRenders.add(consumer);
    return (R) this;
  }

  public R addAfterRenderHook(Consumer<R> consumer) {
    this.afterRenders.add(consumer);
    return (R) this;
  }

  public RenderContext<?, R> getContext() {
    return this.renderContext.get();
  }

  public Renderable<R, T> callAfterRenderHook() {
    for (Consumer<R> afterRender : this.afterRenders) {
      afterRender.accept((R) this);
    }
    return this;
  }

  public Renderable<R, T> callRendererNotSetAction() {
    this.rendererNotSetAction.accept((R) this);
    return this;
  }

  public R setRendererNotSetAction(Consumer<R> rendererNotSetAction) {
    this.rendererNotSetAction = rendererNotSetAction;
    return (R) this;
  }

  public Renderable<R, T> callBeforeRenderHook() {
    for (Consumer<R> beforeRender : this.beforeRenders) {
      beforeRender.accept((R) this);
    }
    return this;
  }

  public T getTarget() {
    return this.target;
  }
}
