package net.flintmc.mcapi.nbt.io.write;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A data output stream lets an application write primitive Java data types and named binary tags to an output stream
 * in a portable way. An application can then use a data input stream to read the data back in.
 */
public interface NBTDataOutputStream {

  /**
   * Writes a full tag to this output stream.
   * <p>
   * (ID, name and value when applicable)
   *
   * @param name The name from the tag.
   * @param nbt  The named binary tag.
   * @throws IOException If an error occurred during writing.
   */
  void writeFullyFormedTag(String name, NBT nbt) throws IOException;

  /**
   * Writes a TAG_End to this output stream.
   *
   * @throws IOException If an error occurred during writing.
   */
  void writeEndTag() throws IOException;

  /**
   * Retrieves a data output stream.
   *
   * @return A data output stream.
   */
  DataOutputStream getDataOutputStream();

  /**
   * A factory class for the {@link NBTDataOutputStream}.
   */
  @AssistedFactory(NBTDataOutputStream.class)
  interface Factory {

    /**
     * Creates a new {@link NBTDataOutputStream} with the given {@link DataOutputStream}.
     *
     * @param dataOutputStream The output stream for the named binary tag.
     * @return A created output stream.
     */
    NBTDataOutputStream create(@Assisted("dataOutputStream") DataOutputStream dataOutputStream);

  }


}
