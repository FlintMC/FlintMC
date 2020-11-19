package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.Renderer;
import net.flintmc.render.model.RendererRepository;

import java.util.HashMap;
import java.util.Map;

@Implement(EntityRenderContext.class)
public class DefaultEntityRenderContext implements EntityRenderContext {

  private final Entity owner;
  private RendererRepository rendererRepository;
  private final Map<String, ModelBoxHolder<Entity, EntityRenderContext>> renderables;
  private final Map<Object, ModelBoxHolder<Entity, EntityRenderContext>> renderablesByTarget;
  private Renderer<
      ModelBoxHolder<Entity, EntityRenderContext>, EntityRenderContext, MinecraftRenderMeta>
      renderer;

  @SuppressWarnings("BindingAnnotationWithoutInject")
  @AssistedInject
  protected DefaultEntityRenderContext(
      @Assisted Entity owner, RendererRepository rendererRepository) {
    this.owner = owner;
    this.rendererRepository = rendererRepository;
    this.renderables = new HashMap<>();
    this.renderablesByTarget = new HashMap<>();
    this.updateRenderer();
  }

  @SuppressWarnings("BindingAnnotationWithoutInject")
  @AssistedInject
  protected DefaultEntityRenderContext(
      @Assisted Entity owner,
      @Assisted Map<String, ModelBoxHolder<Entity, EntityRenderContext>> renderables,
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

  public Renderer<
      ModelBoxHolder<Entity, EntityRenderContext>, EntityRenderContext, MinecraftRenderMeta>
  getRenderer() {
    return this.renderer;
  }

  public Map<String, ModelBoxHolder<Entity, EntityRenderContext>> getRenderables() {
    return this.renderables;
  }

  public ModelBoxHolder<Entity, EntityRenderContext> getRenderableByTarget(Object target) {
    return this.renderablesByTarget.get(target);
  }

  public EntityRenderContext updateRenderer() {
    this.renderer = rendererRepository.getRenderer(this.owner);
    return this;
  }

  public DefaultEntityRenderContext registerRenderable(
      String name, ModelBoxHolder<Entity, EntityRenderContext> renderable) {
    if (this.renderables.containsKey(name))
      throw new IllegalStateException(
          String.format("ModelBox with name %s already registered", name));

    if (this.renderablesByTarget.containsKey(name))
      throw new IllegalStateException("Multiple renderables refer to the same meta object.");

    this.renderables.put(name, renderable);
    this.renderablesByTarget.put(renderable.getMeta(), renderable);
    return this;
  }
}
