package net.flintmc.mcapi.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.render.MinecraftRenderContext;
import net.flintmc.render.model.ModelBoxHolder;

import java.util.Map;

public interface EntityRenderContext
    extends MinecraftRenderContext<
    Entity, EntityRenderContext, ModelBoxHolder<Entity, EntityRenderContext>, Object> {

  @AssistedFactory(EntityRenderContext.class)
  interface Factory {
    EntityRenderContext create(@Assisted Entity owner);

    EntityRenderContext create(
        @Assisted Entity owner,
        @Assisted Map<String, ModelBoxHolder<Entity, EntityRenderContext>> renderables);
  }
}
