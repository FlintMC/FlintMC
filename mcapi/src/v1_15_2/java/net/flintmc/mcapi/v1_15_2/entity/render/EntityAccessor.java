package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.render.model.Renderable;
import net.flintmc.transform.shadow.FieldCreate;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.Map;

@Shadow("net.minecraft.entity.Entity")
@FieldCreate(
    name = "flintRenderables",
    typeName = "java.util.Map",
    defaultValue = "new java.util.HashMap();")
public interface EntityAccessor {

  @FieldGetter("flintRenderables")
  Map<ModelRenderer, Renderable> getFlintRenderables();
}
