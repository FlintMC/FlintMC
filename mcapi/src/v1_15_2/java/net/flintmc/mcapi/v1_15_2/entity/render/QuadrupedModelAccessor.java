package net.flintmc.mcapi.v1_15_2.entity.render;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.model.ModelRenderer;

@Shadow("net.minecraft.client.renderer.entity.model.QuadrupedModel")
public interface QuadrupedModelAccessor {

  @FieldGetter("headModel")
  ModelRenderer getHead();

  @FieldGetter("body")
  ModelRenderer getBody();

  @FieldGetter("legBackRight")
  ModelRenderer getLegBackRight();

  @FieldGetter("legBackLeft")
  ModelRenderer getLegBackLeft();

  @FieldGetter("legFrontRight")
  ModelRenderer getLegFrontRight();

  @FieldGetter("legFrontLeft")
  ModelRenderer getLegFrontLeft();


}
