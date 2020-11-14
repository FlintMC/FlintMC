package net.flintmc.mcapi.internal.entity.render;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.render.EntityModelBox;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.render.model.internal.DefaultModelBox;

@Implement(EntityModelBox.class)
public class DefaultEntityModelBox
    extends DefaultModelBox<Entity, EntityRenderContext, DefaultEntityModelBox, Object>
    implements EntityModelBox {

  @AssistedInject
  protected DefaultEntityModelBox(
      @Assisted EntityRenderContext renderContext, @Assisted Object target) {
    super(renderContext, target);
  }
}
