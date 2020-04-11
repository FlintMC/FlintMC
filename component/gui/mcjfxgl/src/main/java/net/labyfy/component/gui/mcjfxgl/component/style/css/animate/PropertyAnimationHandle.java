package net.labyfy.component.gui.mcjfxgl.component.style.css.animate;

import javafx.beans.value.WritableValue;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.util.Duration;
import net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate.BackgroundInterpolation;
import net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate.BorderInterpolation;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class PropertyAnimationHandle {
  private Supplier<Duration> durationSupplier;
  private double currentDuration;
  private Set<PropertyKeyValue<?>> keyValues;
  private Supplier<Boolean> animationCondition;
  private boolean finished;

  private HashMap<WritableValue<?>, Object> initialValuesMap;
  private HashMap<WritableValue<?>, Object> endValuesMap;

  public PropertyAnimationHandle(
      Supplier<Duration> durationSupplier,
      Supplier<Boolean> animationCondition,
      Set<PropertyKeyValue<?>> keyValues) {
    this.durationSupplier = durationSupplier;
    this.currentDuration = -1;
    this.keyValues = keyValues;
    this.animationCondition = animationCondition;
    this.finished = false;
    this.initialValuesMap = new HashMap<>();
    this.endValuesMap = new HashMap<>();
  }

  public boolean getFinished() {
    return finished;
  }

  public void init() {
    finished = animationCondition == null ? false : animationCondition.get();
    keyValues.stream()
        .filter(Objects::nonNull)
        .forEach(
            keyValue -> {
              if (!initialValuesMap.containsKey(keyValue.getTarget())) {
                initialValuesMap.put(keyValue.getTarget(), keyValue.getTarget().getValue());
              }
              if (!endValuesMap.containsKey(keyValue.getTarget())) {
                endValuesMap.put(keyValue.getTarget(), keyValue.getEndValue());
              }
            });
  }

  public void reverse(double now) {
    if (this.durationSupplier.get() == null) return;
    finished = animationCondition == null ? false : animationCondition.get();
    currentDuration = durationSupplier.get().toMillis() - (currentDuration - now);
    keyValues.forEach(
        keyValue -> {
          final WritableValue<?> target = keyValue.getTarget();
          if (target != null) {
            initialValuesMap.put(target, target.getValue());
            endValuesMap.put(target, keyValue.getEndValue());
          }
        });
  }

  public void animate(double now) {
    if (this.durationSupplier.get() == null) return;
    if (finished) return;
    if (currentDuration == -1) this.currentDuration = this.durationSupplier.get().toMillis();
    if (now <= currentDuration) {
      keyValues.forEach(
          keyValue -> {
            if (keyValue.isValid()) {
              // Stupid Java Generics
              final WritableValue target = keyValue.getTarget();
              final Object endValue = endValuesMap.get(target);
              if (endValue != null
                  && target != null
                  && target.getValue() != null
                  && !target.getValue().equals(endValue)) {
                if (initialValuesMap.get(target) instanceof Border) {
                  target.setValue(
                      BorderInterpolation.getInstance()
                          .interpolate(
                              (Border) initialValuesMap.get(target),
                              (Border) endValue,
                              now / currentDuration,
                              keyValue.getInterpolatorSupplier().get()));
                } else if (initialValuesMap.get(target) instanceof Background) {
                  target.setValue(
                      BackgroundInterpolation.getInstance()
                          .interpolate(
                              (Background) initialValuesMap.get(target),
                              (Background) endValue,
                              currentDuration == 0 ? 0 : now / currentDuration,
                              keyValue.getInterpolatorSupplier().get()));
                } else {
                  target.setValue(
                      keyValue
                          .getInterpolatorSupplier()
                          .get()
                          .interpolate(
                              initialValuesMap.get(target), endValue, now / currentDuration));
                }
              }
            }
          });
    } else {
      finished = true;
      keyValues.forEach(
          keyValue -> {
            if (keyValue.isValid()) {
              // Stupid Java Generics at it again
              final WritableValue target = keyValue.getTarget();
              if (target != null) {
                final Object endValue = keyValue.getEndValue();
                if (endValue != null) {
                  target.setValue(endValue);
                }
              }
            }
          });
      currentDuration = durationSupplier.get().toMillis();
    }
  }

  public void applyEndValues() {
    keyValues.forEach(
        keyValue -> {
          if (keyValue.isValid()) {
            // sike
            final WritableValue target = keyValue.getTarget();
            if (target != null) {
              final Object endValue = keyValue.getEndValue();
              if (endValue != null && !target.getValue().equals(endValue)) {
                target.setValue(endValue);
              }
            }
          }
        });
  }

  public void clear() {
    initialValuesMap.clear();
    endValuesMap.clear();
  }

  public void dispose() {
    clear();
    keyValues.clear();
  }
}
