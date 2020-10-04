package net.labyfy.internal.component.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.gamesettings.frontend.FrontendOption;
import net.labyfy.component.inject.implement.Implement;

/**
 * Default implementation of a {@link FrontendOption}.
 */
@Implement(FrontendOption.class)
public class DefaultFrontendOption implements FrontendOption {

  private final String name;
  private final Class<?> type;
  private final String defaultValue;

  private int min;
  private int max;

  private double minVal;
  private double maxVal;

  @AssistedInject
  private DefaultFrontendOption(
          @Assisted("name") String name,
          @Assisted("type") Class<?> type,
          @Assisted("defaultValue") String defaultValue
  ) {
    this.name = name;
    this.type = type;
    this.defaultValue = defaultValue;
    this.min = this.max = 0;
    this.minVal = this.maxVal = 0D;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getConfigurationName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FrontendOption setRange(int min, int max) {
    this.min = min;
    this.max = max;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMin() {
    return this.min;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMax() {
    return this.max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FrontendOption setRange(double min, double max) {
    this.minVal = min;
    this.maxVal = max;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMinValue() {
    return this.minVal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMaxValue() {
    return this.maxVal;
  }
}
