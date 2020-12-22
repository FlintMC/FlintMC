package net.flintmc.framework.config.internal.storage;

import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorage;

public class ComparableConfigStorage implements Comparable<ComparableConfigStorage>, ConfigStorage {

  private final ConfigStorage wrapped;
  private final int priority;

  private ComparableConfigStorage(ConfigStorage wrapped, int priority) {
    this.wrapped = wrapped;
    this.priority = priority;
  }

  public static ComparableConfigStorage wrap(ConfigStorage storage, int priority) {
    if (storage instanceof ComparableConfigStorage) {
      return (ComparableConfigStorage) storage;
    }

    return new ComparableConfigStorage(storage, priority);
  }

  @Override
  public int compareTo(ComparableConfigStorage o) {
    return Integer.compare(this.priority, o.priority);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.wrapped.getName();
  }

  /** {@inheritDoc} */
  @Override
  public void write(ParsedConfig config) {
    this.wrapped.write(config);
  }

  /** {@inheritDoc} */
  @Override
  public void read(ParsedConfig config) {
    this.wrapped.read(config);
  }
}
