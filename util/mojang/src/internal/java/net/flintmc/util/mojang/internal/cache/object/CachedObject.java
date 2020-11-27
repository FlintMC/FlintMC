package net.flintmc.util.mojang.internal.cache.object;

public class CachedObject<T> {

  private final Class<T> type;
  private final T value;
  private final CachedObjectIO<T> io;
  private final long validUntil;

  public CachedObject(Class<T> type, T value, CachedObjectIO<T> io, long validUntil) {
    this.type = type;
    this.value = value;
    this.io = io;
    this.validUntil = validUntil;
  }

  public Class<T> getType() {
    return this.type;
  }

  public T getValue() {
    return this.value;
  }

  public CachedObjectIO<T> getIO() {
    return this.io;
  }

  public long getValidUntil() {
    return this.validUntil;
  }

  public boolean isValid() {
    return System.currentTimeMillis() <= this.validUntil;
  }
}
