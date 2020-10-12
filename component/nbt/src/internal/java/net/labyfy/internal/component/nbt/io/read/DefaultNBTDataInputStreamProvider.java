package net.labyfy.internal.component.nbt.io.read;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTCreator;
import net.labyfy.component.nbt.io.read.NBTDataInputStream;

import java.io.DataInputStream;

/**
 * Default implementation of the {@link NBTDataInputStream.Provider}.
 */
@Singleton
@Implement(NBTDataInputStream.Provider.class)
public class DefaultNBTDataInputStreamProvider implements NBTDataInputStream.Provider {

  private final NBTDataInputStream.Factory nbtDataInputStreamFactory;
  private final NBTCreator nbtCreator;

  @Inject
  private DefaultNBTDataInputStreamProvider(
          NBTDataInputStream.Factory nbtDataInputStreamFactory,
          NBTCreator nbtCreator
  ) {
    this.nbtDataInputStreamFactory = nbtDataInputStreamFactory;
    this.nbtCreator = nbtCreator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTDataInputStream get(DataInputStream inputStream) {
    return this.nbtDataInputStreamFactory.create(inputStream, this.nbtCreator);
  }
}
