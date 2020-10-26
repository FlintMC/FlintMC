package net.labyfy.internal.component.nbt.io.write;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBT;
import net.labyfy.component.nbt.NBTType;
import net.labyfy.component.nbt.io.write.NBTDataOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Default implementation of the {@link NBTDataOutputStream}.
 */
@Implement(NBTDataOutputStream.class)
public class DefaultNBTDataOutputStream implements NBTDataOutputStream {

  private final DataOutputStream dataOutputStream;

  @AssistedInject
  private DefaultNBTDataOutputStream(@Assisted("dataOutputStream") DataOutputStream dataOutputStream) {
    this.dataOutputStream = dataOutputStream;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeFullyFormedTag(String name, NBT nbt) throws IOException {
    this.dataOutputStream.writeByte(nbt.getIdentifier().getIdentifier());
    this.dataOutputStream.writeUTF(name);
    nbt.writeContents(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeEndTag() throws IOException {
    this.dataOutputStream.writeByte(NBTType.TAG_END.getIdentifier());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataOutputStream getDataOutputStream() {
    return this.dataOutputStream;
  }
}
