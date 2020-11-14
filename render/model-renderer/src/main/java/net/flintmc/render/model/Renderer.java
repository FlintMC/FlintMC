package net.flintmc.render.model;

public interface Renderer<R extends Renderable<?, ?, R, ?>, C extends RenderContext<?, ?, R, ?>> {

  void render(R renderable);
}
