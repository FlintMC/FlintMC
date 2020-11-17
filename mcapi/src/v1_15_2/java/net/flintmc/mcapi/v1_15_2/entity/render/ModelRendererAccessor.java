package net.flintmc.mcapi.v1_15_2.entity.render;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.model.ModelRenderer;

@Shadow("net.minecraft.client.renderer.model.ModelRenderer")
public interface ModelRendererAccessor {

  @FieldGetter("cubeList")
  ObjectList<ModelRenderer.ModelBox> getModelBoxes();
}
