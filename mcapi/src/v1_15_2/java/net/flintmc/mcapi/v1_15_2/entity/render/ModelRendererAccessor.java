package net.flintmc.mcapi.v1_15_2.entity.render;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.flintmc.transform.shadow.FieldCreate;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.flintmc.util.property.Property;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.Map;

@Shadow("net.minecraft.client.renderer.model.ModelRenderer")
@FieldCreate(name = "properties", typeName = "java.util.Map", defaultValue = "new java.util.HashMap()")
public interface ModelRendererAccessor {

  @FieldGetter("cubeList")
  ObjectList<ModelRenderer.ModelBox> getModelBoxes();

  @FieldGetter("textureOffsetX")
  int getTextureOffsetX();

  @FieldSetter("textureOffsetX")
  void setTextureOffsetX(int textureOffsetX);

  @FieldGetter("textureOffsetY")
  int getTextureOffsetY();

  @FieldSetter("textureOffsetY")
  void setTextureOffsetY(int textureOffsetY);

  @FieldGetter("textureWidth")
  float getTextureWidth();

  @FieldSetter("textureWidth")
  void setTextureWidth(float textureWidth);

  @FieldGetter("textureHeight")
  float getTextureHeight();

  @FieldSetter("textureHeight")
  void setTextureHeight(float textureHeight);

  @FieldGetter("properties")
  Map<Property<?, ?>, Object> getProperties();
}
