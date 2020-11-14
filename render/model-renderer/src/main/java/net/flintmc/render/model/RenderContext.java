package net.flintmc.render.model;

import java.util.Map;

public interface RenderContext<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Renderable extends Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Target> {

  T_RenderContextAware getOwner();

  Renderer<T_Renderable, T_RenderContext> getRenderer();

  Map<String, T_Renderable> getRenderables();

  T_RenderContext registerRenderable(String name, T_Renderable renderable);

  T_Renderable getRenderableByTarget(T_Target target);
}
