package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;
import javafx.geometry.Insets;

public class InsetsInterpolation implements Interpolation<Insets> {

  private static final InsetsInterpolation INSTANCE;

  static {
    INSTANCE = new InsetsInterpolation();
  }

  public Insets interpolate(Insets startValue, Insets endValue, double progress, Interpolator interpolator) {

    return new Insets(
      interpolate(Insets::getTop, startValue, endValue, progress, interpolator),
      interpolate(Insets::getRight, startValue, endValue, progress, interpolator),
      interpolate(Insets::getBottom, startValue, endValue, progress, interpolator),
      interpolate(Insets::getLeft, startValue, endValue, progress, interpolator));
  }

  public static InsetsInterpolation getInstance() {
    return INSTANCE;
  }
}
