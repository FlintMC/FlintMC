package net.labyfy.component.nbt.io.read;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.nbt.NBT;
import net.labyfy.component.nbt.NBTCreator;

import java.io.DataInputStream;
import java.io.IOException;

public interface NBTDataInputStream {

  /**
   * Reads a full tag from this input stream.
   * <p>
   * (ID, name and value when applicable)
   *
   * @return The fully formed tag.
   */
  Pair<String, NBT> readFullyFormedTag() throws IOException;

  /**
   * Reads a tag from this input stream.
   *
   * @param identifier The identifier of this named binary tag.
   * @return A new named binary tag.
   */
  NBT readTag(int identifier) throws IOException;

  /**
   * Retrieves a data input stream.
   *
   * @return A data input stream.
   */
  DataInputStream getDataInputStream();

  /**
   * A factory class for the {@link NBTDataInputStream}.
   */
  @AssistedFactory(NBTDataInputStream.class)
  interface Factory {

    /**
     * Creates a new {@link NBTDataInputStream} with the given parameters.
     *
     * @param inputStream The input stream to read.
     * @param nbtCreator  The named binary tag creator to creates tags.a
     * @return A created input stream.
     */
    NBTDataInputStream create(
            @Assisted("inputStream") DataInputStream inputStream,
            @Assisted("nbtCreator") NBTCreator nbtCreator
    );

  }

  /**
   * A service interface for creating {@link NBTDataInputStream}.
   */
  interface Provider {

    /**
     * Creates a new {@link NBTDataInputStream}.
     *
     * @param inputStream The input stream to read.
     * @return A created input stream.
     * @see Factory#create(DataInputStream, NBTCreator)
     */
    NBTDataInputStream get(DataInputStream inputStream);

  }

}
