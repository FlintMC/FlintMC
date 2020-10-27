package net.flintmc.mcapi.internal.nbt.io.write;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.nbt.NBT;
import net.flintmc.mcapi.nbt.io.write.NBTDataOutputStream;
import net.flintmc.mcapi.nbt.io.write.NBTWriter;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Default implementation of the {@link NBTWriter}.
 */
@Implement(NBTWriter.class)
public class DefaultNBTWriter implements NBTWriter {

  private final NBTDataOutputStream outputStream;

  @AssistedInject
  private DefaultNBTWriter(
          NBTDataOutputStream.Factory factory,
          @Assisted("output") OutputStream outputStream,
          @Assisted("compressed") boolean compressed
  ) throws IOException {
    this.outputStream = factory.create(new DataOutputStream(compressed ? new GZIPOutputStream(outputStream) : outputStream));
  }

  @AssistedInject
  private DefaultNBTWriter(
          NBTDataOutputStream.Factory factory,
          @Assisted("output") File file,
          @Assisted("compressed") boolean compressed
  ) throws IOException {
    this(factory, new BufferedOutputStream(new FileOutputStream(file)), compressed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeNamed(String name, NBT tag) throws IOException {
    this.outputStream.writeFullyFormedTag(name, tag);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeRaw(NBT tag) throws IOException {
    tag.writeContents(this.outputStream);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.outputStream.getDataOutputStream().close();
  }
}
