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

  void render(T_Renderable renderable, T_RenderMeta renderMeta);
}
