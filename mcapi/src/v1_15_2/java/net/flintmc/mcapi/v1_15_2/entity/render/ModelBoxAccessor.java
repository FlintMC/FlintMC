package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.client.renderer.model.ModelRenderer$ModelBox")
public interface ModelBoxAccessor {

  @FieldGetter("quads")
  TexturedQuadAccessor[] getQuads();
}
