package net.flintmc.mcapi.nbt.io.read;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.util.commons.Pair;

/**
 * Reads NBT data from a given input stream. Once the input stream is passed to a {@link NBTReader},
 * use {@link #close()} to close the stream.
 */
public interface NBTReader {

  /**
   * Reads a single named tag from the source. `First` will hold the name, `second` the tag.
   *
   * @return A single named tag from the source.
   * @throws IOException If an error occurred during reading.
   */
  Pair<String, NBT> readNamed() throws IOException;

  /**
   * Reads a single tag from the source.
   *
   * @return A single tag from the source.
   * @throws IOException If an error occurred during reading.
   */
  NBT read() throws IOException;

  /**
   * Reads a single tag from the source.
   *
   * @param identifier The tag identifier to read.
   * @return IF an error occurred during reading.
   */
  NBT readRaw(int identifier) throws IOException;

  /**
   * Closes the reader.
   */
  void close();

  /**
   * A factory class for the {@link NBTReader}
   */
  @AssistedFactory(NBTReader.class)
  interface Factory {

    /**
     * Creates a new {@link NBTReader} with the given parameters.
     *
     * @param inputStream The input stream to read the data.
     * @param compressed  {@code true} if the data was compressed otherwise {@code false}.
     * @return A created named binary tag reader.
     */
    NBTReader create(
        @Assisted("input") InputStream inputStream, @Assisted("compressed") boolean compressed);

    /**
     * Creates a new {@link NBTReader} with the given parameters.
     *
     * @param file       The input to read the data.
     * @param compressed {@code true} if the data was compressed otherwise {@code false}.
     * @return A created named binary tag reader.
     */
    NBTReader create(@Assisted("file") File file, @Assisted("compressed") boolean compressed);
  }
}
