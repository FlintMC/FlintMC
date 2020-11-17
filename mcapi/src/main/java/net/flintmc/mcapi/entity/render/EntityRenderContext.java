package net.flintmc.mcapi.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.render.MinecraftRenderContext;
import net.flintmc.render.model.ModelBox;

import java.util.Map;

public interface EntityRenderContext
    extends MinecraftRenderContext<
    Entity, EntityRenderContext, ModelBox<Entity, EntityRenderContext>, Object> {

  @AssistedFactory(EntityRenderContext.class)
  interface Factory {
    EntityRenderContext create(@Assisted Entity owner);

    EntityRenderContext create(
        @Assisted Entity owner,
        @Assisted Map<String, ModelBox<Entity, EntityRenderContext>> renderables);
  }
}
