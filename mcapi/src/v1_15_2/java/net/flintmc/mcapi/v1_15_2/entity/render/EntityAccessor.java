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

package net.flintmc.mcapi.v1_15_2.entity.render;

import java.util.Map;
import net.flintmc.render.model.Renderable;
import net.flintmc.transform.shadow.FieldCreate;
import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.renderer.model.ModelRenderer;

@Shadow("net.minecraft.entity.Entity")
@FieldCreate(
    name = "flintRenderables",
    typeName = "java.util.Map",
    defaultValue = "new java.util.HashMap();")
public interface EntityAccessor {

  @FieldGetter("flintRenderables")
  Map<ModelRenderer, Renderable> getFlintRenderables();
}
