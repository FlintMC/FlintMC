package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;
import javafx.scene.layout.CornerRadii;

public class CornerRadiiInterpolation implements Interpolation<CornerRadii> {

  private static final CornerRadiiInterpolation INSTANCE;

  static {
    INSTANCE = new CornerRadiiInterpolation();
  }

  public CornerRadii interpolate(CornerRadii startRadii, CornerRadii endRadii1, double t, Interpolator interpolator) {
    return new CornerRadii(
      interpolate(CornerRadii::getTopLeftHorizontalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getTopLeftVerticalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getTopRightVerticalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getTopRightHorizontalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getBottomRightHorizontalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getBottomRightVerticalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getBottomLeftVerticalRadius, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::getBottomLeftHorizontalRadius, startRadii, endRadii1, t, interpolator),

      interpolate(CornerRadii::isTopLeftHorizontalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isTopLeftVerticalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isTopRightVerticalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isTopRightHorizontalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isBottomRightHorizontalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isBottomRightVerticalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isBottomLeftVerticalRadiusAsPercentage, startRadii, endRadii1, t, interpolator),
      interpolate(CornerRadii::isBottomLeftHorizontalRadiusAsPercentage, startRadii, endRadii1, t, interpolator)
    );
  }

  public static CornerRadiiInterpolation getInstance() {
    return INSTANCE;
  }

}
