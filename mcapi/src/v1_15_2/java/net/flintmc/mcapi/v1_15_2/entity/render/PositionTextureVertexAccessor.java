package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.Vector3f;

@Shadow("net.minecraft.client.renderer.model.ModelRenderer$PositionTextureVertex")
public interface PositionTextureVertexAccessor {

  @FieldGetter("textureU")
  float getTextureU();

  @FieldGetter("textureV")
  float getTextureV();

  @FieldGetter("position")
  Vector3f getPosition();
}
