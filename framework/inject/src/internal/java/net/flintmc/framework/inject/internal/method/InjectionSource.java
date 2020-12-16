package net.flintmc.framework.inject.internal.method;

import com.google.inject.Key;

public class InjectionSource {

  private final int sourceIndex;
  private final Key<?> key;
  private final Class<?> type;

  public InjectionSource(int sourceIndex, Key<?> key, Class<?> type) {
    this.sourceIndex = sourceIndex;
    this.key = key;
    this.type = type;
  }

  public boolean isFromInjector() {
    return this.sourceIndex == -1;
  }

  public int getSourceIndex() {
    return this.sourceIndex;
  }

  public Key<?> getKey() {
    return this.key;
  }

  public Class<?> getType() {
    return this.type;
  }
}
