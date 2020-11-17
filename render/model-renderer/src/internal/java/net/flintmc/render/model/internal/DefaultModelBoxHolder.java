package net.flintmc.render.model.internal;

import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;

public class DefaultModelBoxHolder<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware,
            T_RenderContext,
            ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
            ?,
            Object>,
    T_Renderable extends
        DefaultModelBoxHolder<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget>
    extends DefaultRenderable<
    T_RenderContextAware,
    T_RenderContext,
    ModelBoxHolder<T_RenderContextAware, T_RenderContext>,
    Object>
    implements ModelBoxHolder<T_RenderContextAware, T_RenderContext> {

  protected DefaultModelBoxHolder(T_RenderContext renderContext, Object meta) {
    super(renderContext, meta);
  }

}
