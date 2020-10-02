package net.labyfy.component.gamesettings.frontend;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface FrontendType {

  String getConfigurationName();

  Class<?> getType();

  FrontendType setRange(int min, int max);

  int getMin();

  int getMax();

  FrontendType setRange(double min, double max);

  double getMinValue();

  double getMaxValue();

  @AssistedFactory(FrontendType.class)
  interface Factory {

    FrontendType create(@Assisted("name") String name, @Assisted("type") Class<?> cls);

  }

}
