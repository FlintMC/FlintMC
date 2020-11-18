package net.flintmc.util.mojang.internal.cache;

import java.util.UUID;

public class UUIDMappedValue<T> {

  private final UUID uniqueId;
  private final T value;

  public UUIDMappedValue(UUID uniqueId, T value) {
    this.uniqueId = uniqueId;
    this.value = value;
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public T getValue() {
    return this.value;
  }
}
