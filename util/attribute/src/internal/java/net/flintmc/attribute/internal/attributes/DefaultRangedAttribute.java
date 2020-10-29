package net.flintmc.attribute.internal.attributes;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.attribute.attributes.RangedAttribute;
import net.flintmc.attribute.internal.DefaultAttribute;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.MathHelper;

/**
 * Default implementation of the {@link RangedAttribute}.
 */
@Implement(RangedAttribute.class)
public class DefaultRangedAttribute extends DefaultAttribute implements RangedAttribute {

  private final double minimumValue;
  private final double maximumValue;
  private String description;

  @AssistedInject
  public DefaultRangedAttribute(
          @Assisted("name") String name,
          @Assisted("defaultValue") double defaultValue,
          @Assisted("minimumValue") double minimumValue,
          @Assisted("maximumValue") double maximumValue) {
    super(name, defaultValue);
    this.minimumValue = minimumValue;
    this.maximumValue = maximumValue;

    if (minimumValue > maximumValue) {
      throw new IllegalArgumentException("The minimum value cannot be bigger than maximum value!");
    } else if (defaultValue > minimumValue) {
      throw new IllegalArgumentException("The default value cannot be lower than minimum value!");
    } else if (defaultValue > maximumValue) {
      throw new IllegalArgumentException("The default value cannot be bigger than maximum value!");
    }
  }

  @AssistedInject
  public DefaultRangedAttribute(
          @Assisted("name") String name,
          @Assisted("defaultValue") double defaultValue,
          @Assisted("description") String description,
          @Assisted("minimumValue") double minimumValue,
          @Assisted("maximumValue") double maximumValue) {
    super(name, defaultValue);
    this.minimumValue = minimumValue;
    this.maximumValue = maximumValue;
    this.description = description;

    if (minimumValue > maximumValue) {
      throw new IllegalArgumentException("The minimum value cannot be bigger than maximum value!");
    } else if (defaultValue > minimumValue) {
      throw new IllegalArgumentException("The default value cannot be lower than minimum value!");
    } else if (defaultValue > maximumValue) {
      throw new IllegalArgumentException("The default value cannot be bigger than maximum value!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RangedAttribute setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double clampValue(double value) {
    return MathHelper.clamp(value, this.minimumValue, this.maximumValue);
  }
}
