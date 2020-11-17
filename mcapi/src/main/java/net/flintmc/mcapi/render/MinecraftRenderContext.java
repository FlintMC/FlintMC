package net.flintmc.mcapi.render;

import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;
import net.flintmc.render.model.Renderable;

public interface MinecraftRenderContext<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        MinecraftRenderContext<
            T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget>
    extends RenderContext<
    T_RenderContextAware, T_RenderContext, T_Renderable, MinecraftRenderMeta, T_RenderTarget> {
}
