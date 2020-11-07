package net.flintmc.mcapi.internal.nbt;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBTFloat;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/** Default implementation the {@link NBTFloat}. */
@Implement(NBTFloat.class)
public class DefaultNBTFloat implements NBTFloat {

  private float value;

  @AssistedInject
  private DefaultNBTFloat(@Assisted("value") float value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_FLOAT;
  }

  /** {@inheritDoc} */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readFloat();
  }

  /** {@inheritDoc} */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeFloat(this.value);
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return String.valueOf(value);
  }

  /** {@inheritDoc} */
  @Override
  public float asFloat() {
    return this.value;
  }
}
