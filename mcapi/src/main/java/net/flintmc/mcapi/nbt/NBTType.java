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

package net.flintmc.mcapi.nbt;

public enum NBTType {
  TAG_END(0),
  TAG_BYTE(1),
  TAG_SHORT(2),
  TAG_INT(3),
  TAG_LONG(4),
  TAG_FLOAT(5),
  TAG_DOUBLE(6),
  TAG_BYTE_ARRAY(7),
  TAG_STRING(8),
  TAG_LIST(9),
  TAG_COMPOUND(10),
  TAG_INT_ARRAY(11),
  TAG_LONG_ARRAY(12),
  ;

  private final int identifier;

  NBTType(int identifier) {
    this.identifier = identifier;
  }

  public static NBTType getNbt(int identifier) {
    for (NBTType value : values()) {
      if (value.identifier == identifier) {
        return value;
      }
    }
    return null;
  }

  public int getIdentifier() {
    return identifier;
  }
}
