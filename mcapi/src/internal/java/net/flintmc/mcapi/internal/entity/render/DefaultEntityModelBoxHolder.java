package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityModelBoxHolder;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.render.model.internal.DefaultModelBoxHolder;

@Implement(EntityModelBoxHolder.class)
public class DefaultEntityModelBoxHolder
    extends DefaultModelBoxHolder<Entity, EntityRenderContext, DefaultEntityModelBoxHolder, Object>
    implements EntityModelBoxHolder {

  @AssistedInject
  protected DefaultEntityModelBoxHolder(
      @Assisted EntityRenderContext renderContext, @Assisted Object target) {
    super(renderContext, target);
  }
}
