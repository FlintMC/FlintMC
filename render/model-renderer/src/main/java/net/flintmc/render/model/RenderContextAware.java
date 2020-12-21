package net.flintmc.render.model;

/**
 * Defines a component that provides a {@link RenderContext}.
 *
 * @param <T_RenderContext> the component that coordinates the rendering process.
 */
public interface RenderContextAware<T_RenderContext extends RenderContext<?, ?, ?, ?, ?>> {

  /**
   * @return the provided {@link RenderContext}
   */
  T_RenderContext getRenderContext();

  void updateRenderables();
}
