package net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate;

import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.css.StyleableObjectProperty;
import javafx.util.Duration;
import net.labyfy.component.gui.mcjfxgl.component.style.css.animate.PropertyAnimationTimer;
import net.labyfy.component.gui.mcjfxgl.component.style.css.animate.PropertyKeyFrame;
import net.labyfy.component.gui.mcjfxgl.component.style.css.animate.PropertyKeyValue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class expects original to extend both {@link ObservableValue} and {@link
 * javafx.beans.property.Property}. Sadly there is no simple solution to expect both from Javafx..
 * Yes.. {@link javafx.css.StyleableProperty} does not implement {@link
 * javafx.beans.property.Property}. Why? I have no fucking idea. Because of that this Solution is
 * implemented a little bit ugly.
 */
public class PropertyInterpolator<T> {

  private final ObservableValue<T> original;
  private final StyleableObjectProperty<Duration> duration;
  private final StyleableObjectProperty<T> dummy;
  private final ObjectProperty<Interpolator> interpolator;
  private final AtomicBoolean interpolating = new AtomicBoolean(false);

  private final PropertyAnimationTimer animationTimer;
  private T nextEndValue;
  private boolean initialized;
  private Interpolator lastInterpolator = Interpolator.EASE_BOTH;
  private final Object lock = new Object();

  public PropertyInterpolator(
    ObservableValue<T> original,
    StyleableObjectProperty<Duration> duration,
    StyleableObjectProperty<T> dummy,
    ObjectProperty<Interpolator> interpolator) {
    this.original = original;
    this.duration = duration;
    this.dummy = dummy;
    this.interpolator = interpolator;
    this.animationTimer = new PropertyAnimationTimer(this.createAnimationKeyFrame());
    this.wire();
  }

  private PropertyKeyFrame createAnimationKeyFrame() {
    return PropertyKeyFrame.builder()
      .withDurationSupplier(this.duration::get)
      .withKeyValues(
        PropertyKeyValue.builder()
          .withTarget(this.dummy)
          .withEndValueSupplier(() -> this.nextEndValue)
          .withInterpolatorSupplier(() -> this.lastInterpolator)
          .build())
      .build();
  }

  private void wire() {
    this.wireInterpolatorSpline();

    this.original.addListener(
        (observable, oldValue, newValue) -> {
          synchronized (lock) {
            if (!interpolating.get()) {
              interpolating.set(true);
              PropertyInterpolator.this.nextEndValue = newValue;
              animationTimer.reverseAndContinue();
              if (PropertyInterpolator.this.initialized) {
                ((WritableValue) PropertyInterpolator.this.original).setValue(oldValue);
              } else {
                PropertyInterpolator.this.initialized = true;
              }
              interpolating.set(false);
            }
          }
        });

    this.dummy.addListener(
      (observable, oldValue, newValue) -> {
        synchronized (lock) {
          interpolating.set(true);
          ((WritableValue) PropertyInterpolator.this.original).setValue(newValue);
          interpolating.set(false);
        }
      });

  }

  private void wireInterpolatorSpline() {
    this.interpolator.addListener(
      (observable, oldValue, newValue) -> {
        if (newValue instanceof Interpolator) {
          this.lastInterpolator = newValue;
        }
      });
  }

  public PropertyAnimationTimer getAnimationTimer() {
    return animationTimer;
  }
}
