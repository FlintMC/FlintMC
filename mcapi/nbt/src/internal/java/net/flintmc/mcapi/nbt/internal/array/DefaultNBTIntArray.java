package net.flintmc.mcapi.nbt.internal.array;

import com.google.common.base.Joiner;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.array.NBTIntArray;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;
import java.util.Collections;

/**
 * Default implementation of the {@link NBTIntArray}.
 */
@Implement(NBTIntArray.class)
public class DefaultNBTIntArray implements NBTIntArray {

  private int[] value;

  @AssistedInject
  private DefaultNBTIntArray(@Assisted("value") int[] value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_INT_ARRAY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    int length = inputStream.getDataInputStream().readInt();
    this.value = new int[length];

    for (int i = 0; i < length; i++) {
      this.value[i] = inputStream.getDataInputStream().readInt();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeInt(this.value.length);
    for (int i = 0; i < this.value.length; i++) {
      outputStream.getDataOutputStream().writeInt(this.value[i]);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String asString() {
    return Joiner.on(",").join(Collections.singleton(this.value));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int[] asArray() {
    return this.value;
  }
}
