package net.labyfy.component.gui.mcjfxgl.component.style.css.animate;

import javafx.animation.Interpolator;
import javafx.beans.value.WritableValue;

import java.util.function.Supplier;

public class PropertyKeyValue<T> {

  private WritableValue<T> target;
  private Supplier<WritableValue<T>> targetSupplier;
  private Supplier<T> endValueSupplier;
  private T endValue;
  private Supplier<Boolean> animationCondition = () -> true;
  private Supplier<Interpolator> interpolatorSupplier;

  private PropertyKeyValue() {}

  public static TypeInferredBuilder builder() {
    return new TypeInferredBuilder();
  }

  public WritableValue<T> getTarget() {
    return target == null ? targetSupplier.get() : target;
  }

  public T getEndValue() {
    return endValue == null ? endValueSupplier.get() : endValue;
  }

  public Supplier<Boolean> getAnimationCondition() {
    return animationCondition;
  }

  public Supplier<Interpolator> getInterpolatorSupplier() {
    return interpolatorSupplier;
  }

  public boolean isValid() {
    return animationCondition == null ? true : animationCondition.get();
  }

  public static final class LabyKeyValueBuilder<T> {

    private WritableValue<T> target;
    private Supplier<WritableValue<T>> targetSupplier;
    private Supplier<T> endValueSupplier;
    private T endValue;
    private Supplier<Boolean> animationCondition = () -> true;
    private Supplier<Interpolator> interpolatorSupplier;

    private LabyKeyValueBuilder() {}

    public LabyKeyValueBuilder<T> withTarget(WritableValue<T> target) {
      this.target = target;
      return this;
    }

    public LabyKeyValueBuilder<T> withTargetSupplier(Supplier<WritableValue<T>> targetSupplier) {
      this.targetSupplier = targetSupplier;
      return this;
    }

    public LabyKeyValueBuilder<T> withEndValueSupplier(Supplier<T> endValueSupplier) {
      this.endValueSupplier = endValueSupplier;
      return this;
    }

    public LabyKeyValueBuilder<T> withEndValue(T endValue) {
      this.endValue = endValue;
      return this;
    }

    public LabyKeyValueBuilder<T> withAnimationCondition(Supplier<Boolean> animationCondition) {
      this.animationCondition = animationCondition;
      return this;
    }

    public LabyKeyValueBuilder<T> withInterpolatorSupplier(
        Supplier<Interpolator> interpolatorSupplier) {
      this.interpolatorSupplier = interpolatorSupplier;
      return this;
    }

    public LabyKeyValueBuilder<T> withInterpolator(Interpolator interpolator) {
      return this.withInterpolatorSupplier(() -> interpolator);
    }

    public PropertyKeyValue<T> build() {
      PropertyKeyValue<T> labyKeyValue = new PropertyKeyValue<>();
      labyKeyValue.target = this.target;
      labyKeyValue.interpolatorSupplier = this.interpolatorSupplier;
      labyKeyValue.endValueSupplier = this.endValueSupplier;
      labyKeyValue.animationCondition = this.animationCondition;
      labyKeyValue.endValue = this.endValue;
      labyKeyValue.targetSupplier = this.targetSupplier;
      return labyKeyValue;
    }
  }

  public static final class TypeInferredBuilder {

    public <T> LabyKeyValueBuilder<T> withTarget(WritableValue<T> target) {
      return new LabyKeyValueBuilder<T>().withTarget(target);
    }

    public <T> LabyKeyValueBuilder<T> withTargetSupplier(
        Supplier<WritableValue<T>> targetSupplier) {
      return new LabyKeyValueBuilder<T>().withTargetSupplier(targetSupplier);
    }

    public <T> LabyKeyValueBuilder<T> withEndValueSupplier(Supplier<T> endValueSupplier) {
      return new LabyKeyValueBuilder<T>().withEndValueSupplier(endValueSupplier);
    }

    public <T> LabyKeyValueBuilder<T> withEndValue(T endValue) {
      return new LabyKeyValueBuilder<T>().withEndValue(endValue);
    }

    public <T> LabyKeyValueBuilder<T> withAnimationCondition(Supplier<Boolean> animationCondition) {
      return new LabyKeyValueBuilder<T>().withAnimationCondition(animationCondition);
    }

    public <T> LabyKeyValueBuilder<T> withInterpolator(Interpolator interpolator) {
      return new LabyKeyValueBuilder<T>().withInterpolator(interpolator);
    }
  }
}
