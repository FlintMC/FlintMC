package net.flintmc.render.model;

import java.util.Map;

public interface RenderContext<O, R extends Renderable<R, ?>> {

  O getOwner();

  RenderContext<O, R> setRenderer(Renderer<R, RenderContext<O, R>> renderer);

  RenderContext<O, R> setRenderer(Class<? extends Renderer<R, RenderContext<O, R>>> rendererType);

  Renderer<R, RenderContext<O, R>> getRenderer();

  Map<String, R> getRenderables();

}
