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

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;

@Shadow("net.minecraft.util.math.vector.Matrix3f")
public interface Matrix3fAccessor {

  @FieldGetter("m00")
  float getM00();

  @FieldGetter("m01")
  float getM01();

  @FieldGetter("m02")
  float getM02();

  @FieldGetter("m10")
  float getM10();

  @FieldGetter("m11")
  float getM11();

  @FieldGetter("m12")
  float getM12();

  @FieldGetter("m20")
  float getM20();

  @FieldGetter("m21")
  float getM21();

  @FieldGetter("m22")
  float getM22();
}
