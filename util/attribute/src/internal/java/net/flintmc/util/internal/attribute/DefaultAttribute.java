package net.flintmc.util.internal.attribute;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.util.attribute.Attribute;

public class DefaultAttribute implements Attribute {

  private final String key;
  private final float defaultValue;
  private final float maximumValue;
  private final boolean shared;

  @AssistedInject
  public DefaultAttribute(
      @Assisted("key") String key,
      @Assisted("defaultValue") float defaultValue,
      @Assisted("maximumVale") float maximumValue) {
    this(key, defaultValue, maximumValue, true);
  }

  @AssistedInject
  public DefaultAttribute(
      @Assisted("key") String key,
      @Assisted("defaultValue") float defaultValue,
      @Assisted("maximumValue") float maximumValue,
      @Assisted("shared") boolean shared) {
    if (defaultValue > maximumValue) {
      throw new IllegalArgumentException(
          String.format(
              "Default value cannot be greater than the maximum allowed. (Default: %s, Maximum: %s)",
              defaultValue, maximumValue));
    }
    this.key = key;
    this.defaultValue = defaultValue;
    this.maximumValue = maximumValue;
    this.shared = shared;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return this.key;
  }

  /** {@inheritDoc} */
  @Override
  public float getDefaultValue() {
    return this.defaultValue;
  }

  /** {@inheritDoc} */
  @Override
  public float getMaximumValue() {
    return this.maximumValue;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isShared() {
    return this.shared;
  }
}
