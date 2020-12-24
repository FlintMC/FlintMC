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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.*;
import net.flintmc.mcapi.nbt.array.NBTByteArray;
import net.flintmc.mcapi.nbt.array.NBTIntArray;
import net.flintmc.mcapi.nbt.array.NBTLongArray;

/** Default implementation the {@link NBTCreator}. */
@Singleton
@Implement(NBTCreator.class)
public class DefaultNBTCreator implements NBTCreator {

  private final NBTByte.Factory nbtByteFactory;
  private final NBTCompound.Factory nbtCompoundFactory;
  private final NBTDouble.Factory nbtDoubleFactory;
  private final NBTEnd.Factory nbtEndFactory;
  private final NBTFloat.Factory nbtFloatFactory;
  private final NBTInt.Factory nbtIntFactory;
  private final NBTList.Factory nbtListFactory;
  private final NBTLong.Factory nbtLongFactory;
  private final NBTShort.Factory nbtShortFactory;
  private final NBTString.Factory nbtStringFactory;

  private final NBTByteArray.Factory nbtByteArrayFactory;
  private final NBTIntArray.Factory nbtIntArrayFactory;
  private final NBTLongArray.Factory nbtLongArrayFactory;

  @Inject
  private DefaultNBTCreator(
      NBTByte.Factory nbtByteFactory,
      NBTCompound.Factory nbtCompoundFactory,
      NBTDouble.Factory nbtDoubleFactory,
      NBTEnd.Factory nbtEndFactory,
      NBTFloat.Factory nbtFloatFactory,
      NBTInt.Factory nbtIntFactory,
      NBTList.Factory nbtListFactory,
      NBTLong.Factory nbtLongFactory,
      NBTShort.Factory nbtShortFactory,
      NBTString.Factory nbtStringFactory,
      NBTByteArray.Factory nbtByteArrayFactory,
      NBTIntArray.Factory nbtIntArrayFactory,
      NBTLongArray.Factory nbtLongArrayFactory) {
    this.nbtByteFactory = nbtByteFactory;
    this.nbtCompoundFactory = nbtCompoundFactory;
    this.nbtDoubleFactory = nbtDoubleFactory;
    this.nbtEndFactory = nbtEndFactory;
    this.nbtFloatFactory = nbtFloatFactory;
    this.nbtIntFactory = nbtIntFactory;
    this.nbtListFactory = nbtListFactory;
    this.nbtLongFactory = nbtLongFactory;
    this.nbtShortFactory = nbtShortFactory;
    this.nbtStringFactory = nbtStringFactory;
    this.nbtByteArrayFactory = nbtByteArrayFactory;
    this.nbtIntArrayFactory = nbtIntArrayFactory;
    this.nbtLongArrayFactory = nbtLongArrayFactory;
  }

  /** {@inheritDoc} */
  @Override
  public NBTByte createNbtByte(byte value) {
    return this.nbtByteFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound createNbtCompound() {
    return this.nbtCompoundFactory.create();
  }

  /** {@inheritDoc} */
  @Override
  public NBTDouble createNbtDouble(double value) {
    return this.nbtDoubleFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTEnd createNbtEnd() {
    return this.nbtEndFactory.create();
  }

  /** {@inheritDoc} */
  @Override
  public NBTFloat createNbtFloat(float value) {
    return this.nbtFloatFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTInt createNbtInt(int value) {
    return this.nbtIntFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTList createNbtList(int subtagIdentifier) {
    return this.nbtListFactory.create(subtagIdentifier);
  }

  /** {@inheritDoc} */
  @Override
  public NBTLong createNbtLong(long value) {
    return this.nbtLongFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTShort createNbtShort(short value) {
    return this.nbtShortFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTString createNbtString(String value) {
    return this.nbtStringFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTByteArray createNbtByteArray(byte[] value) {
    return this.nbtByteArrayFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTIntArray createNbtIntArray(int[] value) {
    return this.nbtIntArrayFactory.create(value);
  }

  /** {@inheritDoc} */
  @Override
  public NBTLongArray createNbtLongArray(long[] value) {
    return this.nbtLongArrayFactory.create(value);
  }
}
