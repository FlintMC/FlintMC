package net.flintmc.mcapi.internal.nbt.io.read;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.util.commons.Pair;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.NBTCreator;
import net.flintmc.mcapi.nbt.NBTList;
import net.flintmc.mcapi.nbt.NBTType;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Default implementation of the {@link NBTDataInputStream}.
 */
@Implement(NBTDataInputStream.class)
public class DefaultNBTDataInputStream implements NBTDataInputStream {

  private final DataInputStream dataInputStream;
  private final NBTCreator nbtCreator;

  @AssistedInject
  private DefaultNBTDataInputStream(
          @Assisted("inputStream") DataInputStream dataInputStream,
          NBTCreator nbtCreator
  ) {
    this.dataInputStream = dataInputStream;
    this.nbtCreator = nbtCreator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Pair<String, NBT> readFullyFormedTag() throws IOException {
    int identifier = this.dataInputStream.readByte();

    if (identifier == NBTType.TAG_END.getIdentifier()) {
      return new Pair<>("", this.nbtCreator.createNbtEnd());
    }
    String name = this.dataInputStream.readUTF();

    return new Pair<>(name, this.readTag(identifier));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBT readTag(int identifier) throws IOException {
    NBTType type = NBTType.getNbt(identifier);

    if (type == null) {
      throw new NullPointerException("");
    }

    switch (type) {
      case TAG_END:
        return this.nbtCreator.createNbtEnd();
      case TAG_BYTE:
        return this.nbtCreator.createNbtByte((byte) 0);
      case TAG_SHORT:
        return this.nbtCreator.createNbtShort((short) 0);
      case TAG_INT:
        return this.nbtCreator.createNbtInt(0);
      case TAG_LONG:
        return this.nbtCreator.createNbtLong(0L);
      case TAG_FLOAT:
        return this.nbtCreator.createNbtFloat(0.0F);
      case TAG_DOUBLE:
        return this.nbtCreator.createNbtDouble(0.0D);
      case TAG_BYTE_ARRAY:
        return this.nbtCreator.createNbtByteArray(new byte[0]);
      case TAG_STRING:
        return this.nbtCreator.createNbtString("");
      case TAG_LIST:
        int subtagType = this.dataInputStream.readByte();
        NBTList list = this.nbtCreator.createNbtList(subtagType);
        list.readContents(this);
        return list;
      case TAG_COMPOUND:
        return this.nbtCreator.createNbtCompound();
      case TAG_INT_ARRAY:
        return this.nbtCreator.createNbtIntArray(new int[0]);
      case TAG_LONG_ARRAY:
        return this.nbtCreator.createNbtLongArray(new long[0]);
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataInputStream getDataInputStream() {
    return this.dataInputStream;
  }
}
