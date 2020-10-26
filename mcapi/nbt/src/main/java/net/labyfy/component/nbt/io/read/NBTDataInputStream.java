package net.labyfy.component.nbt.io.read;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.nbt.NBT;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * A data input stream lets an application read primitive Java data types and named binary tags from an
 * underlying input stream in a machine-independent way. An application ues a data output stream to write data that can
 * later be read by a data input stream.
 */
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
     * @return A created input stream.
     */
    NBTDataInputStream create(@Assisted("inputStream") DataInputStream inputStream);

  }

}
