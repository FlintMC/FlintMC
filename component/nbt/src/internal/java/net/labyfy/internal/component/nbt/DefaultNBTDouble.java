package net.labyfy.internal.component.nbt;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTDouble;
import net.labyfy.component.nbt.NBTType;
import net.labyfy.component.nbt.io.read.NBTDataInputStream;
import net.labyfy.component.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/**
 * Default implementation the {@link NBTDouble}.
 */
@Implement(NBTDouble.class)
public class DefaultNBTDouble implements NBTDouble {

  private double value;

  @AssistedInject
  private DefaultNBTDouble(@Assisted("value") double value) {
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_DOUBLE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {
    this.value = inputStream.getDataInputStream().readDouble();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {
    outputStream.getDataOutputStream().writeDouble(this.value);
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
  public double asDouble() {
    return this.value;
  }
}
