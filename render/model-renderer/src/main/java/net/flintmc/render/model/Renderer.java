package net.flintmc.render.model;

public interface Renderer<
    T_Renderable extends Renderable<?, ?, T_Renderable, ?>,
    T_RenderContext extends RenderContext<?, ?, T_Renderable, T_RenderMeta, ?>,
    T_RenderMeta> {

  void render(T_Renderable renderable, T_RenderMeta renderMeta);
}
