package net.flintmc.framework.inject.internal.method.optimizer;

public class InjectionSource {

  private final int sourceIndex;
  private final Class<?> type;

  public InjectionSource(int sourceIndex, Class<?> type) {
    this.sourceIndex = sourceIndex;
    this.type = type;
  }

  public boolean isFromInjector() {
    return this.sourceIndex == -1;
  }

  public Class<?> getType() {
    return this.type;
  }

  public int getSourceIndex() {
    return this.sourceIndex;
  }
}
