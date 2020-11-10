package net.flintmc.mcapi.internal.nbt.io.read;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.io.read.NBTDataInputStream;
import net.flintmc.mcapi.nbt.io.read.NBTReader;
import net.flintmc.util.commons.Pair;

import java.io.*;
import java.util.zip.GZIPInputStream;

/** Default implementation of the {@link NBTReader}. */
@Implement(NBTReader.class)
public class DefaultNBTReader implements NBTReader {

  private final NBTDataInputStream inputStream;

  @AssistedInject
  private DefaultNBTReader(
      NBTDataInputStream.Factory factory,
      @Assisted("input") InputStream inputStream,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this.inputStream =
        factory.create(
            new DataInputStream(compressed ? new GZIPInputStream(inputStream) : inputStream));
  }

  @AssistedInject
  private DefaultNBTReader(
      NBTDataInputStream.Factory provider,
      @Assisted("file") File file,
      @Assisted("compressed") boolean compressed)
      throws IOException {
    this(provider, new BufferedInputStream(new FileInputStream(file)), compressed);
  }

  /** {@inheritDoc} */
  @Override
  public Pair<String, NBT> readNamed() throws IOException {
    return this.inputStream.readFullyFormedTag();
  }

  /** {@inheritDoc} */
  @Override
  public NBT read() throws IOException {
    return this.readNamed().getSecond();
  }

  /** {@inheritDoc} */
  @Override
  public NBT readRaw(int identifier) throws IOException {
    return this.inputStream.readTag(identifier);
  }

  /** {@inheritDoc} */
  @Override
  public void close() {
    try {
      this.inputStream.getDataInputStream().close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
