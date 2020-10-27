package net.flintmc.mcapi.nbt.internal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTShort;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/**
 * Default implementation the {@link NBTShort}.
 */
@Implement(NBTShort.class)
public class DefaultNBTShort implements NBTShort {

  private short value;

  @AssistedInject
  private DefaultNBTShort(@Assisted("value") short value) {
    this.value = value;
  }

  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_INT;
  }

  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readShort();
  }

  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeShort(this.value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String asString() {
    return String.valueOf(this.value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public short asShort() {
    return this.value;
  }
}
