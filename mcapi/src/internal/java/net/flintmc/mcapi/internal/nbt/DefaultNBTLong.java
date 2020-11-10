package net.flintmc.mcapi.internal.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTLong;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/** Default implementation the {@link NBTLong}. */
@Implement(NBTLong.class)
public class DefaultNBTLong implements NBTLong {

  private long value;

  @AssistedInject
  private DefaultNBTLong(@Assisted("value") long value) {
    this.value = value;
  }

  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_LONG;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readLong();
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeLong(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return String.valueOf(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public long asLong() {
    return this.value;
  }
}
