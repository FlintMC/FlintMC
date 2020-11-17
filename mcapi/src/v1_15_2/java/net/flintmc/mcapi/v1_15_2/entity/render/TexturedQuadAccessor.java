package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.Vector3f;

@Shadow("net.minecraft.client.renderer.model.ModelRenderer$TexturedQuad")
public interface TexturedQuadAccessor {

  @FieldGetter("vertexPositions")
  PositionTextureVertexAccessor[] getVertexPositions();

  @FieldGetter("normal")
  Vector3f getNormal();
}
