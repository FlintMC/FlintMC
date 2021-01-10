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

package net.flintmc.mcapi.internal.nbt.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.NBTByte;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.nbt.NBTCreator;
import net.flintmc.mcapi.nbt.NBTDouble;
import net.flintmc.mcapi.nbt.NBTEnd;
import net.flintmc.mcapi.nbt.NBTFloat;
import net.flintmc.mcapi.nbt.NBTInt;
import net.flintmc.mcapi.nbt.NBTList;
import net.flintmc.mcapi.nbt.NBTLong;
import net.flintmc.mcapi.nbt.NBTShort;
import net.flintmc.mcapi.nbt.NBTString;
import net.flintmc.mcapi.nbt.serializer.NBTSerializer;

/**
 * Default implementation of the {@link NBTSerializer}.
 */
@Singleton
@Implement(NBTSerializer.class)
public class DefaultNBTSerializer implements NBTSerializer {

  private final NBTCreator creator;

  @Inject
  private DefaultNBTSerializer(NBTCreator creator) {
    this.creator = creator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonElement serialize(NBT nbt) {

    if (nbt instanceof NBTByte) {
      NBTByte nbtByte = (NBTByte) nbt;
      return new JsonPrimitive(nbtByte.asByte());
    } else if (nbt instanceof NBTDouble) {
      NBTDouble nbtByte = (NBTDouble) nbt;
      return new JsonPrimitive(nbtByte.asDouble());
    } else if (nbt instanceof NBTFloat) {
      NBTFloat nbtByte = (NBTFloat) nbt;
      return new JsonPrimitive(nbtByte.asFloat());
    } else if (nbt instanceof NBTInt) {
      NBTInt nbtByte = (NBTInt) nbt;
      return new JsonPrimitive(nbtByte.asInt());
    } else if (nbt instanceof NBTLong) {
      NBTLong nbtByte = (NBTLong) nbt;
      return new JsonPrimitive(nbtByte.asLong());
    } else if (nbt instanceof NBTShort) {
      NBTShort nbtByte = (NBTShort) nbt;
      return new JsonPrimitive(nbtByte.asShort());
    } else if (nbt instanceof NBTString) {
      NBTString nbtString = (NBTString) nbt;
      return new JsonPrimitive(nbtString.asString());
    } else if (nbt instanceof NBTList) {
      NBTList list = (NBTList) nbt;

      JsonArray array = new JsonArray();

      for (NBT namedBinaryTag : list) {
        array.add(this.serialize(namedBinaryTag));
      }

      return array;
    } else if (nbt instanceof NBTCompound) {
      NBTCompound compound = (NBTCompound) nbt;
      JsonObject object = new JsonObject();

      for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
        object.add(entry.getKey(), this.serialize(entry.getValue()));
      }
      return object;
    } else if (nbt instanceof NBTEnd) {
      throw new AssertionError();
    }

    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBT deserialize(JsonElement element) {
    if (element instanceof JsonPrimitive) {
      JsonPrimitive primitive = (JsonPrimitive) element;

      if (primitive.isBoolean()) {
        boolean value = primitive.getAsBoolean();

        return this.creator.createNbtByte((byte) (value ? 0 : 1));
      } else if (primitive.isNumber()) {
        Number number = primitive.getAsNumber();

        if (number instanceof Byte) {
          return this.creator.createNbtByte(number.byteValue());
        } else if (number instanceof Short) {
          return this.creator.createNbtShort(number.shortValue());
        } else if (number instanceof Integer) {
          return this.creator.createNbtInt(number.intValue());
        } else if (number instanceof Long) {
          return this.creator.createNbtLong(number.longValue());
        } else if (number instanceof Float) {
          return this.creator.createNbtFloat(number.floatValue());
        } else if (number instanceof Double) {
          return this.creator.createNbtDouble(number.doubleValue());
        }
      } else if (primitive.isString()) {
        return this.creator.createNbtString(primitive.getAsString());
      }
    } else if (element instanceof JsonArray) {
      JsonArray array = (JsonArray) element;
      NBTList list = this.creator.createNbtList(0);

      for (JsonElement jsonElement : array) {
        list.add(this.deserialize(jsonElement));
      }

      return list;
    } else if (element instanceof JsonObject) {
      JsonObject object = (JsonObject) element;
      NBTCompound compound = this.creator.createNbtCompound();

      for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
        compound.set(entry.getKey(), this.deserialize(entry.getValue()));
      }

      return compound;
    } else if (element instanceof JsonNull) {
      return this.creator.createNbtCompound();
    }

    throw new AssertionError();
  }
}
