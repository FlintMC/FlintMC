package net.labyfy.component.nbt.io.write;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.nbt.NBT;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes NBT data to a given destination.
 * Once the output is passed to a {@link NBTWriter}, use {@link #close()} to close the stream.
 */
public interface NBTWriter {

  /**
   * Writes a tag with a name inside the destination.
   *
   * @param name The name to write.
   * @param tag  The tag to write.
   */
  void writeNamed(String name, NBT tag) throws IOException;

  /**
   * Writes a tag contents directly to the destination.
   *
   * @param tag The tag to write.
   */
  void writeRaw(NBT tag) throws IOException;

  /**
   * Closes the writer.
   */
  void close() throws IOException;


  /**
   * A factory class for the {@link NBTWriter}.
   */
  @AssistedFactory(NBTWriter.class)
  interface Factory {

    /**
     * Creates a new {@link NBTWriter} with the given parameters.
     *
     * @param factory      The factory for the output stream.
     * @param outputStream The output stream for the datas.
     * @param compressed   {@code true} if the data should be compressed, otherwise {@code false}.
     * @return A created named binary tag writer.
     */
    NBTWriter create(
            @Assisted("factory") NBTDataOutputStream.Factory factory,
            @Assisted("output") OutputStream outputStream,
            @Assisted("compressed") boolean compressed
    );

    /**
     * Creates a new {@link NBTWriter} with the given parameters.
     *
     * @param factory    The factory for the output stream.
     * @param file       The output for the datas.
     * @param compressed {@code true} if the data should be compressed, otherwise {@code false}.
     * @return A created named binary tag writer.
     */
    NBTWriter create(
            @Assisted("factory") NBTDataOutputStream.Factory factory,
            @Assisted("output") File file,
            @Assisted("compressed") boolean compressed
    );

  }
}
