package net.flintmc.render.model;

import java.util.Map;

/**
 * Defines an abstract component that can be rendered.
 *
 * @param <T_RenderContextAware> the component that is the "owner" of the provided render context.
 * @param <T_RenderContext>      the type of this component. Just used here to lock the generic types of
 *                               the system to ensure type safety.
 * @param <T_Renderable>         the type of the {@link Renderable} to handle.
 * @param <T_RenderMeta>         the type of metadata that must be provided to the {@link Renderer}.
 * @param <T_RenderTarget>       the target type of the {@link Renderable}.
 */
public interface RenderContext<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta, T_RenderTarget>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderMeta,
    T_RenderTarget> {

  /**
   * @return the provided owner of this {@link RenderContext}
   */
  T_RenderContextAware getOwner();

  /**
   * @return the {@link Renderer} matching this {@link RenderContext}. Will be fetched by {@link
   * RendererRepository#getRenderer(RenderContextAware)}
   */
  Renderer<T_Renderable, T_RenderContext, T_RenderMeta> getRenderer();

  /**
   * @return all registered {@link Renderable}s.
   */
  Map<String, T_Renderable> getRenderables();

  /**
   * Registers a {@link Renderable} to this {@link RenderContext}. Registered {@link Renderable}s
   * will be returned by {@link RenderContext#getRenderables()} and {@link
   * RenderContext#getRenderableByTarget(Object)}
   *
   * @param name
   * @param renderable
   * @return this
   */
  T_RenderContext registerRenderable(String name, T_Renderable renderable);

  /**
   * @param target the given target to look for
   * @return the {@link Renderable} that references the given target
   */
  T_Renderable getRenderableByTarget(T_RenderTarget target);

  /**
   * Re-fetches the {@link Renderer} from {@link RendererRepository}. This will change the output of
   * {@link RenderContext#getRenderer()} if a new {@link Renderer} was found.
   *
   * @return this
   */
  T_RenderContext updateRenderer();
}
