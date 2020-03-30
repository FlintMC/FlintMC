package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;

import java.util.LinkedList;
import java.util.List;

public class BorderInterpolation implements Interpolation<Border> {

  private static final BorderInterpolation INSTANCE;

  static {
    INSTANCE = new BorderInterpolation();
  }

  public Border interpolate(
    Border startValue, Border endValue, double t, Interpolator interpolator) {
    if (t <= 0.0) return startValue;
    if (t >= 1.0) return endValue;

    if (startValue.getStrokes().size() != endValue.getStrokes().size()) return endValue;

    List<BorderStroke> startStrokes = startValue.getStrokes();
    List<BorderStroke> endStrokes = endValue.getStrokes();

    List<BorderStroke> newStrokes = new LinkedList<>();
    for (int i = 0; i < startStrokes.size(); i++) {
      BorderStroke startStroke = startStrokes.get(i);
      BorderStroke endStroke = endStrokes.get(i);
      newStrokes.add(this.interpolateStroke(startStroke, endStroke, t, interpolator));
    }

    return new Border(newStrokes.toArray(new BorderStroke[]{}));
  }

  private BorderStroke interpolateStroke(
    BorderStroke startStroke, BorderStroke endStroke, double t, Interpolator interpolator) {
    return new BorderStroke(
      interpolate(BorderStroke::getTopStroke, startStroke, endStroke, t, interpolator),
      interpolate(BorderStroke::getRightStroke, startStroke, endStroke, t, interpolator),
      interpolate(BorderStroke::getBottomStroke, startStroke, endStroke, t, interpolator),
      interpolate(BorderStroke::getLeftStroke, startStroke, endStroke, t, interpolator),
      endStroke.getTopStyle(),
      endStroke.getRightStyle(),
      endStroke.getBottomStyle(),
      endStroke.getLeftStyle(),
      CornerRadiiInterpolation.getInstance().interpolate(startStroke.getRadii(), endStroke.getRadii(), t, interpolator),
      interpolateWidths(startStroke.getWidths(), endStroke.getWidths(), t, interpolator),
      InsetsInterpolation.getInstance().interpolate(startStroke.getInsets(), endStroke.getInsets(), t, interpolator));
  }


  private BorderWidths interpolateWidths(BorderWidths startWidth, BorderWidths endWidth, double t, Interpolator interpolator) {
    return new BorderWidths(
      interpolate(BorderWidths::getTop, startWidth, endWidth, t, interpolator),
      interpolate(BorderWidths::getRight, startWidth, endWidth, t, interpolator),
      interpolate(BorderWidths::getBottom, startWidth, endWidth, t, interpolator),
      interpolate(BorderWidths::getLeft, startWidth, endWidth, t, interpolator));
  }


  public static BorderInterpolation getInstance() {
    return INSTANCE;
  }
}
