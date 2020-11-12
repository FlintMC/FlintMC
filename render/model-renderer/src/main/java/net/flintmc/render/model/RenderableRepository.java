package net.flintmc.render.model;

public interface RenderableRepository {

  RenderableRepository register(Renderable<?, ?> renderable);

  RenderableRepository render(Object owner);

}
