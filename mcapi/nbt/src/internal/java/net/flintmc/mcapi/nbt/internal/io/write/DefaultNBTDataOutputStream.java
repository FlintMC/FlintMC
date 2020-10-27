package net.flintmc.mcapi.nbt.internal.io.write;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;

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
