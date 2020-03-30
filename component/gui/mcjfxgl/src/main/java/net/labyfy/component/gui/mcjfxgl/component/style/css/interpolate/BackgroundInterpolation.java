package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class BackgroundInterpolation implements Interpolation<Background> {


  private static final BackgroundInterpolation INSTANCE;

  static {
    INSTANCE = new BackgroundInterpolation();
  }

  public Background interpolate(Background startValue, Background endValue, double progress, Interpolator interpolator) {
    return new Background(this.interpolateBackgroundFills(startValue.getFills(), endValue.getFills(), progress, interpolator).toArray(new BackgroundFill[]{}));
  }

  private List<BackgroundFill> interpolateBackgroundFills(List<BackgroundFill> backgroundFillsStart, List<BackgroundFill> backgroundFillsEnd, double progress, Interpolator interpolator) {
    List<BackgroundFill> backgroundFillCollections = new ArrayList<>();
    assert backgroundFillsStart.size() == backgroundFillsEnd.size();
    for (int i = 0; i < backgroundFillsStart.size(); i++) {
      backgroundFillCollections.add(this.interpolateBackgroundFill(backgroundFillsStart.get(i), backgroundFillsEnd.get(i), progress, interpolator));
    }

    return backgroundFillCollections;
  }

  private BackgroundFill interpolateBackgroundFill(BackgroundFill start, BackgroundFill end, double progress, Interpolator interpolator) {
    return new BackgroundFill(
      (Paint) interpolator.interpolate(start.getFill(), end.getFill(), progress),
      CornerRadiiInterpolation.getInstance().interpolate(start.getRadii(), end.getRadii(), progress, interpolator),
      InsetsInterpolation.getInstance().interpolate(start.getInsets(), end.getInsets(), progress, interpolator));
  }

  public static BackgroundInterpolation getInstance() {
    return INSTANCE;
  }

}
