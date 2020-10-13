package net.labyfy.internal.component.nbt;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTEnd;
import net.labyfy.component.nbt.NBTType;
import net.labyfy.component.nbt.io.read.NBTDataInputStream;
import net.labyfy.component.nbt.io.write.NBTDataOutputStream;

import java.io.IOException;

/**
 * Default implementation the {@link NBTEnd}.
 */
@Implement(NBTEnd.class)
public class DefaultNBTEnd implements NBTEnd {

  @AssistedInject
  private DefaultNBTEnd() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTType getIdentifier() {
    return NBTType.TAG_END;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readContents(NBTDataInputStream inputStream) throws IOException {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeContents(NBTDataOutputStream outputStream) throws IOException {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String asString() {
    return "";
  }
}
