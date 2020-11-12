package net.flintmc.mcapi.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.render.model.ModelBox;
import net.flintmc.render.model.RenderContext;

import java.util.Map;

public interface EntityRenderContext extends RenderContext<Entity, ModelBox> {

  @AssistedFactory(EntityRenderContext.class)
  interface Factory{
    EntityRenderContext create(@Assisted Entity owner, @Assisted Map<String, ModelBox> renderers);
  }
}
