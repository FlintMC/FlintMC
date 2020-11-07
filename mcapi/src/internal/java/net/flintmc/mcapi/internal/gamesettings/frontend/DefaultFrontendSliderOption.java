package net.flintmc.mcapi.internal.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.frontend.FrontendSliderOption;

/** Default implementation of a {@link FrontendSliderOption}. */
@Implement(FrontendSliderOption.class)
public class DefaultFrontendSliderOption extends DefaultFrontendOption
    implements FrontendSliderOption {

  private int min;
  private int max;

  private double minVal;
  private double maxVal;

  @AssistedInject
  protected DefaultFrontendSliderOption(
      @Assisted("name") String name,
      @Assisted("type") Class<?> type,
      @Assisted("defaultValue") String defaultValue) {
    super(name, type, defaultValue);
    this.min = this.max = 0;
    this.minVal = this.maxVal = 0D;
  }

  /** {@inheritDoc} */
  @Override
  public FrontendSliderOption setRange(int min, int max) {
    this.min = min;
    this.max = max;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public int getMin() {
    return this.min;
  }

  /** {@inheritDoc} */
  @Override
  public int getMax() {
    return this.max;
  }

  /** {@inheritDoc} */
  @Override
  public FrontendSliderOption setRange(double min, double max) {
    this.minVal = min;
    this.maxVal = max;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public double getMinValue() {
    return this.minVal;
  }

  /** {@inheritDoc} */
  @Override
  public double getMaxValue() {
    return this.maxVal;
  }
}
