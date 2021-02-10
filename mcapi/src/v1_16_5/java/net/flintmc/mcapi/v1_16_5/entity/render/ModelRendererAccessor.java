/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_5.entity.render;

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
