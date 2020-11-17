package net.flintmc.render.model;

public interface RenderContextAware<T_RenderContext extends RenderContext<?, ?, ?, ?, ?>> {

  T_RenderContext getRenderContext();

}
