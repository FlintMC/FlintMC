package net.flintmc.mcapi.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.render.model.ModelBoxHolder;

public interface EntityModelBoxHolder extends ModelBoxHolder<Entity, EntityRenderContext> {

  @AssistedFactory(EntityModelBoxHolder.class)
  interface Factory {
    EntityModelBoxHolder create(@Assisted EntityRenderContext renderContext, @Assisted Object target);
  }

}
