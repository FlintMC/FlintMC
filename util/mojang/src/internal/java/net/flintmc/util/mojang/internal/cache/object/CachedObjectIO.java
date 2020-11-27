package net.flintmc.util.mojang.internal.cache.object;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public interface CachedObjectIO<T> {

  Class<T> getType();

  void write(UUID uniqueId, T t, DataOutput output) throws IOException;

  T read(UUID uniqueId, DataInput input) throws IOException;
}
