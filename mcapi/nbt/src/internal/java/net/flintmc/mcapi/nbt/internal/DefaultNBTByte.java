package net.flintmc.mcapi.nbt.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTByte;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/**
 * Default implementation of the {@link NBTByte}.
 */
@Implement(NBTByte.class)
public class DefaultNBTByte implements NBTByte {

  private byte value;

  @AssistedInject
  private DefaultNBTByte(@Assisted("value") byte value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_BYTE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readByte();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeByte(this.value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String asString() {
    return String.valueOf(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte asByte() {
    return this.value;
  }
}
