package net.flintmc.mcapi.internal.nbt.array;

import com.google.common.base.Joiner;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.array.NBTLongArray;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;
import java.util.Collections;

/** Default implementation of the {@link NBTLongArray}. */
@Implement(NBTLongArray.class)
public class DefaultNBTLongArray implements NBTLongArray {

  private long[] value;

  @AssistedInject
  private DefaultNBTLongArray(@Assisted("value") long[] value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_LONG_ARRAY;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    int length = inputStream.getDataInputStream().readInt();
    this.value = new long[length];

    for (int i = 0; i < length; i++) {
      this.value[i] = inputStream.getDataInputStream().readLong();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeInt(this.value.length);
    for (int i = 0; i < this.value.length; i++) {
      outputStream.getDataOutputStream().writeLong(this.value[i]);
    }
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return Joiner.on(",").join(Collections.singleton(this.value));
  }

  /** {@inheritDoc} */
  @Override
  public long[] asArray() {
    return this.value;
  }
}
