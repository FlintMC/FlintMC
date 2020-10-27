package net.flintmc.mcapi.nbt;

import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;

import java.io.IOException;

/**
 * The basic representation of a NBT tag.
 */
public interface NBT {

  /**
   * Retrieves the identifier of this tag type.
   *
   * @return The identifier of this tag type.
   */
  NBTType getIdentifier();

  /**
   * Reads the contents of the tag from the given source. The tag {@link #getIdentifier()} is supposed to be
   * already read.
   * <p>
   * For {@link NBTList}, it assumes the subtag type identifier as already been read.
   *
   * @param inputStream The input stream to read.
   * @throws IOException If an error occurred during reading
   */
  void readContents(NBTDataInputStream inputStream) throws IOException;

  /**
   * Writes the contents of the tag to the given destination. The tag {@link #getIdentifier()} is supposed to be already
   * written.
   *
   * @param outputStream The output stream to write.
   * @throws IOException If an error occurred during writing.
   */
  void writeContents(NBTDataOutputStream outputStream) throws IOException;

  /**
   * Retrieves the named binary tag content as a {@link String}
   *
   * @return The content of a named binary tag as a string.
   */
  String asString();
}
