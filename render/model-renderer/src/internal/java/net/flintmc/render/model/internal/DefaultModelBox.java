package net.flintmc.render.model.internal;

import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;

public class DefaultModelBox<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<
            T_RenderContextAware,
            T_RenderContext,
            ModelBox<T_RenderContextAware, T_RenderContext>,
            ?,
            Object>,
    T_Renderable extends
        DefaultModelBox<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget>
    extends DefaultRenderable<
    T_RenderContextAware,
    T_RenderContext,
    ModelBox<T_RenderContextAware, T_RenderContext>,
    Object>
    implements ModelBox<T_RenderContextAware, T_RenderContext> {

  protected DefaultModelBox(T_RenderContext renderContext, Object meta) {
    super(renderContext, meta);
  }

}
