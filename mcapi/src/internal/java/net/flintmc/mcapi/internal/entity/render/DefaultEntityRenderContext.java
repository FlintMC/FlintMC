/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.internal.entity.render;

import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.render.model.Renderer;
import net.flintmc.render.model.RendererRepository;

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
    this.renderer = rendererRepository.getRenderer(this);
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
    this.renderablesByTarget.put(renderable.getTarget(), renderable);
    return this;
  }
}
