package net.flintmc.util.mojang.internal.cache.object;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface CachedObjectIO<T> {

  Class<T> getType();

  void write(T t, DataOutput output) throws IOException;

  T read(DataInput input) throws IOException;
}
