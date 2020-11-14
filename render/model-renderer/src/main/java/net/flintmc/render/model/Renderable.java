package net.flintmc.render.model;

import java.util.function.Consumer;

public interface Renderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Renderable extends Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_Target>,
    T_Target> {

  T_Target getTarget();

  T_RenderContext getContext();

  T_Renderable callRenderPreparations();

  T_Renderable callPropertyHandler();

  T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer);

  T_Renderable setPropertyHandler(Consumer<T_Renderable> consumer);
}
