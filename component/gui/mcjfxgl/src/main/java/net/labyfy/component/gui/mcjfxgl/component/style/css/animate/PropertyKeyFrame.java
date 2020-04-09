package net.labyfy.component.gui.mcjfxgl.component.style.css.animate;

import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

public class PropertyKeyFrame {

  private Supplier<Duration> durationSupplier;
  private Set<PropertyKeyValue<?>> keyValues;
  private Supplier<Boolean> animateCondition;

  public PropertyKeyFrame(Duration duration, PropertyKeyValue<?>... keyValues) {
    this(() -> duration, keyValues);
  }

  public PropertyKeyFrame(Supplier<Duration> durationSupplier, PropertyKeyValue<?>... keyValues) {
    this.keyValues = new CopyOnWriteArraySet<>();
    this.animateCondition = null;
    this.durationSupplier = durationSupplier;
    Arrays.stream(keyValues)
            .filter(Objects::nonNull)
            .forEach(keyValue -> this.keyValues.add(keyValue));
  }

  private PropertyKeyFrame() {}

  public static LabyKeyFrameBuilder builder() {
    return new LabyKeyFrameBuilder();
  }

  public Set<PropertyKeyValue<?>> getKeyValues() {
    return keyValues;
  }

  public Supplier<Duration> getDurationSupplier() {
    return durationSupplier;
  }

  public Supplier<Boolean> getAnimateCondition() {
    return animateCondition;
  }

  public static final class LabyKeyFrameBuilder {
    private Supplier<Duration> durationSupplier;
    private Set<PropertyKeyValue<?>> keyValues;
    private Supplier<Boolean> animateCondition;

    private LabyKeyFrameBuilder() {}

    public LabyKeyFrameBuilder withDuration(Duration duration) {
      return this.withDurationSupplier(() -> duration);
    }

    public LabyKeyFrameBuilder withDurationSupplier(Supplier<Duration> duration) {
      this.durationSupplier = duration;
      return this;
    }

    public LabyKeyFrameBuilder withKeyValues(PropertyKeyValue<?>... keyValues) {
      this.keyValues = new HashSet<>(Arrays.asList(keyValues));
      return this;
    }

    public LabyKeyFrameBuilder withAnimateCondition(Supplier<Boolean> animateCondition) {
      this.animateCondition = animateCondition;
      return this;
    }

    public PropertyKeyFrame build() {
      PropertyKeyFrame labyKeyFrame = new PropertyKeyFrame();
      labyKeyFrame.durationSupplier = this.durationSupplier;
      labyKeyFrame.animateCondition = this.animateCondition;
      labyKeyFrame.keyValues = this.keyValues;
      return labyKeyFrame;
    }
  }
}
