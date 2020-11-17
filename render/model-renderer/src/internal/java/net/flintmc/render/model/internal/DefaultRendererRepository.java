package net.flintmc.render.model.internal;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Singleton
@Implement(RendererRepository.class)
public class DefaultRendererRepository implements RendererRepository {

  private final Map<Class<? extends RenderContextAware<?>>, Renderer<?, ?, ?>>
      classDefinedRenderers = new HashMap<>();
  private final Map<Predicate<RenderContext<?, ?, ?, ?, ?>>, Renderer<?, ?, ?>> predicates =
      new HashMap<>();
  private Renderer<?, ?, ?> defaultRenderer;

  @SuppressWarnings({"unchecked"})
  public <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  Renderer<T_Renderable, T_RenderContext, T_RenderMeta> getRenderer(
      T_RenderContextAware renderContextAware) {
    Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer = null;
    for (Map.Entry<Predicate<RenderContext<?, ?, ?, ?, ?>>, Renderer<?, ?, ?>> entry :
        this.predicates.entrySet()) {
      Predicate<RenderContext<?, ?, ?, ?, ?>> predicate = entry.getKey();
      if (predicate.test(renderContextAware.getRenderContext())) {
        if (renderer != null)
          throw new IllegalStateException(
              String.format(
                  "Multiple default renderers for render context aware of type %s found.",
                  renderContextAware.getClass()));
        renderer = (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>) entry.getValue();
      }
    }
    if (renderer != null) return renderer;

    if (this.classDefinedRenderers.containsKey(renderContextAware.getClass()))
      return (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>)
          this.classDefinedRenderers.get(renderContextAware.getClass());

    return (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>) this.defaultRenderer;
  }

  public <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> rendererClass) {
    return this.setRenderer(
        renderContextAwareClass, InjectionHolder.getInjectedInstance(rendererClass));
  }

  public <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer) {
    if (this.classDefinedRenderers.containsKey(renderContextAwareClass))
      throw new IllegalStateException(
          "Renderer for render context aware "
              + renderContextAwareClass.getName()
              + " was already defined as "
              + this.classDefinedRenderers.get(renderContextAwareClass).getClass().getName());

    this.classDefinedRenderers.put(renderContextAwareClass, renderer);
    return this;
  }

  public <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?, ?>> renderablePredicate,
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> renderer) {
    return this.setRenderer(renderablePredicate, InjectionHolder.getInjectedInstance(renderer));
  }

  public <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?, ?>> renderablePredicate,
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer) {
    this.predicates.put(renderablePredicate, renderer);
    return this;
  }

  public Renderer<?, ?, ?> getDefaultRenderer() {
    return defaultRenderer;
  }

  public DefaultRendererRepository setDefaultRenderer(Renderer<?, ?, ?> defaultRenderer) {
    this.defaultRenderer = defaultRenderer;
    return this;
  }
}
