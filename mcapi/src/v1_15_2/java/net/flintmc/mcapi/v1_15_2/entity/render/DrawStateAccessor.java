package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.mcapi.render.MinecraftRenderMeta;
import net.flintmc.render.model.ModelBoxHolder;
import net.flintmc.transform.shadow.FieldCreate;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.client.renderer.BufferBuilder$DrawState")
@FieldCreate(name = "modelBoxHolder", typeName = "net.flintmc.render.model.ModelBoxHolder")
@FieldCreate(name = "modelRenderData", typeName = "net.flintmc.mcapi.render.MinecraftRenderMeta")
public interface DrawStateAccessor {

  @FieldSetter("modelBoxHolder")
  void setModelBoxHolder(ModelBoxHolder<?, ?> modelBoxHolder);

  @FieldGetter("modelBoxHolder")
  ModelBoxHolder<?, ?> getModelBoxHolder();

  @FieldGetter("modelRenderData")
  MinecraftRenderMeta getRenderData();

  @FieldSetter("modelRenderData")
  void setModelRenderData(MinecraftRenderMeta modelRenderData);

}