package net.flintmc.attribute.internal;

import net.flintmc.attribute.Attribute;

/**
 * Default implementation of the {@link Attribute}.
 */
public abstract class DefaultAttribute implements Attribute {

  private final String name;
  private final double defaultValue;
  private boolean shouldWatch;

  protected DefaultAttribute(String name, double defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldWatch() {
    return this.shouldWatch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute setShouldWatch(boolean shouldWatch) {
    this.shouldWatch = shouldWatch;
    return this;
  }
}
