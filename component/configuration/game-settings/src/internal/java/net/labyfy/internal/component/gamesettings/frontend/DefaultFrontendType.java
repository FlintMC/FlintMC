package net.labyfy.internal.component.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.gamesettings.frontend.FrontendType;
import net.labyfy.component.inject.implement.Implement;

@Implement(FrontendType.class)
public class DefaultFrontendType implements FrontendType {

  private final String name;
  private final Class<?> type;

  private int min;
  private int max;

  private double minVal;
  private double maxVal;

  @AssistedInject
  private DefaultFrontendType(@Assisted("name") String name, @Assisted("type") Class<?> type) {
    this.name = name;
    this.type = type;
    this.min = this.max = 0;
    this.minVal = this.maxVal = 0D;
  }

  @Override
  public String getConfigurationName() {
    return this.name;
  }

  @Override
  public Class<?> getType() {
    return this.type;
  }

  @Override
  public FrontendType setRange(int min, int max) {
    this.min = min;
    this.max = max;
    return this;
  }

  @Override
  public int getMin() {
    return this.min;
  }

  @Override
  public int getMax() {
    return this.max;
  }

  @Override
  public FrontendType setRange(double min, double max) {
    this.minVal = min;
    this.maxVal = max;
    return this;
  }

  @Override
  public double getMinValue() {
    return this.minVal;
  }

  @Override
  public double getMaxValue() {
    return this.maxVal;
  }
}
