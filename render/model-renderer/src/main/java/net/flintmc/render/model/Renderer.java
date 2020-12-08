package net.flintmc.render.model;

/**
 * Defines the component in the rendering chain to draw a {@link Renderable} to the current opengl
 * context
 *
 * @param <T_Renderable>    the type of the {@link Renderable} to handle.
 * @param <T_RenderContext> the component that coordinates the rendering process.
 * @param <T_RenderMeta>    the type of metadata that must be provided to this {@link Renderer}.
 */
public interface Renderer<
    T_Renderable extends Renderable<?, ?, T_Renderable, ?>,
    T_RenderContext extends RenderContext<?, ?, T_Renderable, T_RenderMeta, ?>,
    T_RenderMeta> {

  /**
   * Render the given {@link Renderable}. The context in which this will be called depends on the
   * implementation of the underlying layers. All Flint implementations will always provide a valid
   * OpenGL State for the target {@link Renderable}.
   *
   * @param renderable the {@link Renderable} that should be rendered
   * @param renderMeta the metadata required to render the renderable. If not needed, use {@link
   *                   Void}
   */
  void render(T_Renderable renderable, T_RenderMeta renderMeta);

  /**
   * Defines if this {@link Renderer} ends the current render round.
   *
   * @param renderable the {@link Renderable} that should be rendered
   * @param renderMeta the metadata required to render the renderable. If not needed, use {@link
   *                   Void}
   * @return this
   */
  boolean shouldExecuteNextStage(T_Renderable renderable, T_RenderMeta renderMeta);
}
