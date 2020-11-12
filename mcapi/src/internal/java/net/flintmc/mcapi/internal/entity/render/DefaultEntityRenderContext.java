package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.Renderer;

import java.util.Collections;
import java.util.Map;

@Implement(EntityRenderContext.class)
public class DefaultEntityRenderContext implements EntityRenderContext {

  private final Entity owner;
  private final Map<String, ModelBox> renderers;
  private Renderer<ModelBox, RenderContext<Entity, ModelBox>> renderer;

  @AssistedInject
  private DefaultEntityRenderContext(@Assisted Entity owner, @Assisted Map<String, ModelBox> renderers) {
    this.owner = owner;
    this.renderers = Collections.unmodifiableMap(renderers);
    this.renderer = (renderable, renderContext) -> {
      System.out.println("Render123");
    };
  }

  public Entity getOwner() {
    return this.owner;
  }

  public RenderContext<Entity, ModelBox> setRenderer(Class<? extends Renderer<ModelBox, RenderContext<Entity, ModelBox>>> rendererType) {
    return this.setRenderer(InjectionHolder.getInjectedInstance(rendererType));
  }

  public RenderContext<Entity, ModelBox> setRenderer(Renderer<ModelBox, RenderContext<Entity, ModelBox>> renderer) {
    this.renderer = renderer;
    return this;
  }

  public Renderer<ModelBox, RenderContext<Entity, ModelBox>> getRenderer() {
    return this.renderer;
  }

  public Map<String, ModelBox> getRenderables() {
    return this.renderers;
  }
}
