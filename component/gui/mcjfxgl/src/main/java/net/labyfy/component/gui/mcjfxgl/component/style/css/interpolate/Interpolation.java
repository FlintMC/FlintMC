package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;

import java.util.function.Function;

@FunctionalInterface
public interface Interpolation<T> {

  T interpolate(T startValue, T endValue, double progress, Interpolator interpolator);

  default <T, K> T interpolate(
          Function<K, T> converter, K startValue, K endValue, double t, Interpolator interpolator) {
    return (T) interpolator.interpolate(converter.apply(startValue), converter.apply(endValue), t);
  }
}
