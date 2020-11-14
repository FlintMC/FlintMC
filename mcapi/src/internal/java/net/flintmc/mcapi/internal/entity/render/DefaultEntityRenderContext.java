package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.Renderer;
import net.flintmc.render.model.RendererRepository;

import java.util.HashMap;
import java.util.Map;

@Implement(EntityRenderContext.class)
public class DefaultEntityRenderContext implements EntityRenderContext {

  private final Entity owner;
  private final Map<String, ModelBox<Entity, EntityRenderContext>> renderables;
  private final Renderer<ModelBox<Entity, EntityRenderContext>, EntityRenderContext> renderer;
  private final Map<Object, ModelBox<Entity, EntityRenderContext>> renderablesByTarget;

  @SuppressWarnings("BindingAnnotationWithoutInject")
  @AssistedInject
  protected DefaultEntityRenderContext(
      @Assisted Entity owner,
      RendererRepository rendererRepository) {
    this.owner = owner;
    this.renderables = new HashMap<>();
    this.renderablesByTarget = new HashMap<>();
    this.renderer = rendererRepository.getRenderer(this.owner);
  }

  @SuppressWarnings("BindingAnnotationWithoutInject")
  @AssistedInject
  protected DefaultEntityRenderContext(
      @Assisted Entity owner,
      @Assisted Map<String, ModelBox<Entity, EntityRenderContext>> renderables,
      RendererRepository rendererRepository) {
    this.owner = owner;
    this.renderables = new HashMap<>();
    renderables.forEach(this::registerRenderable);
    this.renderablesByTarget = new HashMap<>();
    this.renderer = rendererRepository.getRenderer(this.owner);
  }

  public Entity getOwner() {
    return this.owner;
  }

  public Renderer<ModelBox<Entity, EntityRenderContext>, EntityRenderContext> getRenderer() {
    return this.renderer;
  }

  public Map<String, ModelBox<Entity, EntityRenderContext>> getRenderables() {
    return this.renderables;
  }

  public ModelBox<Entity, EntityRenderContext> getRenderableByTarget(Object target) {
    return this.renderablesByTarget.get(target);
  }

  public DefaultEntityRenderContext registerRenderable(
      String name, ModelBox<Entity, EntityRenderContext> renderable) {
    if (this.renderables.containsKey(name))
      throw new IllegalStateException(
          String.format("ModelBox with name %s already registered", name));

    if (this.renderablesByTarget.containsKey(name))
      throw new IllegalStateException("Multiple renderables target the same object.");

    this.renderables.put(name, renderable);
    this.renderablesByTarget.put(renderable.getTarget(), renderable);
    return this;
  }
}
