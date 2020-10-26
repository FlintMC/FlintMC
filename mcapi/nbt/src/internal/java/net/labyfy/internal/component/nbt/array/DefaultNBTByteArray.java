package net.labyfy.internal.component.nbt.array;

import com.google.common.base.Joiner;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTType;
import net.labyfy.component.nbt.array.NBTByteArray;
import net.labyfy.component.nbt.io.read.NBTDataInputStream;
import net.labyfy.component.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;
import java.util.Collections;

/**
 * Default implementation of the {@link NBTByteArray}.
 */
@Implement(NBTByteArray.class)
public class DefaultNBTByteArray implements NBTByteArray {

  private byte[] value;

  @AssistedInject
  private DefaultNBTByteArray(@Assisted("value") byte[] value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_BYTE_ARRAY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    int length = inputStream.getDataInputStream().readInt();
    this.value = new byte[length];

    for (int i = 0; i < length; i++) {
      this.value[i] = inputStream.getDataInputStream().readByte();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeInt(this.value.length);
    for (int i = 0; i < this.value.length; i++) {
      outputStream.getDataOutputStream().writeByte(this.value[i]);
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
  public byte[] asArray() {
    return this.value;
  }
}
