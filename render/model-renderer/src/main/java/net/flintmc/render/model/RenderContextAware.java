package net.flintmc.render.model;

public interface RenderContextAware<C extends RenderContext<?, ?>> {

  C getRenderContext();

}
