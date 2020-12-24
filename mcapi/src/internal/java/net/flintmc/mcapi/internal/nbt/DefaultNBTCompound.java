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

package net.flintmc.mcapi.internal.nbt;

import com.google.common.collect.Maps;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.nbt.NBTEnd;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;
import net.flintmc.util.commons.Pair;

import java.io.IOException;
import java.util.Map;

/** Default implementation the {@link NBTCompound}. */
@Implement(NBTCompound.class)
public class DefaultNBTCompound implements NBTCompound {

  private final Map<String, NBT> tags;

  @AssistedInject
  private DefaultNBTCompound() {
    this.tags = Maps.newConcurrentMap();
  }

  /** {@inheritDoc} */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_COMPOUND;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    Pair<String, NBT> tag;
    do {
      tag = inputStream.readFullyFormedTag();
      if (!(tag.getSecond() instanceof NBTEnd)) {
        this.tags.put(tag.getFirst(), tag.getSecond());
      }
    } while (!(tag.getSecond() instanceof NBTEnd));
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    for (Map.Entry<String, NBT> entry : this.tags.entrySet()) {
      outputStream.writeFullyFormedTag(entry.getKey(), entry.getValue());
    }
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public int getSize() {
    return this.tags.size();
  }

  /** {@inheritDoc} */
  @Override
  public boolean containsKey(String key) {
    return this.tags.containsKey(key);
  }

  /** {@inheritDoc} */
  @Override
  public NBT get(String key) {
    return this.tags.get(key);
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound set(String key, NBT tag) {
    this.tags.put(key, tag);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, NBT> getTags() {
    return this.tags;
  }
}
