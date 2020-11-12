package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Shadow("net.minecraft.client.renderer.entity.LivingRenderer")
public interface LivingRendererAccessor {

  @FieldGetter("entityModel")
  EntityModel<? extends LivingEntity> getEntityModel();

}
