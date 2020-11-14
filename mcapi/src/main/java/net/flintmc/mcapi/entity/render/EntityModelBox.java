package net.flintmc.mcapi.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.render.model.ModelBox;

public interface EntityModelBox extends ModelBox<Entity, EntityRenderContext> {

  @AssistedFactory(EntityModelBox.class)
  interface Factory {
    EntityModelBox create(@Assisted EntityRenderContext renderContext, @Assisted Object target);
  }
}
