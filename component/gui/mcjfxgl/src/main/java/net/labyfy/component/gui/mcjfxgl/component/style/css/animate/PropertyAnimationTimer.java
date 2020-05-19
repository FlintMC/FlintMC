package net.labyfy.component.gui.mcjfxgl.component.style.css.animate;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Supplier;

public class PropertyAnimationTimer {

  private Set<PropertyAnimationHandle> animationHandlers;
  private long startTime;
  private boolean running;
  private List<PropertyAnimationCacheMemento> cacheMementos;
  private double elapsedMillis;
  private HashMap<PropertyKeyFrame, PropertyAnimationHandle> mutableFrames;

  public PropertyAnimationTimer(PropertyKeyFrame... keyFrames) {
    this.animationHandlers = new HashSet<>();
    this.startTime = -1;
    this.running = false;
    this.cacheMementos = new ArrayList<>();
    this.mutableFrames = new HashMap<>();
    Arrays.stream(keyFrames)
        .filter(v -> !v.getKeyValues().isEmpty())
        .forEach(
            keyFrame ->
                animationHandlers.add(
                    new PropertyAnimationHandle(
                        keyFrame.getDurationSupplier(),
                        keyFrame.getAnimateCondition(),
                        keyFrame.getKeyValues())));
  }

  public void addKeyFrame(PropertyKeyFrame keyFrame) throws Exception {
    if (isRunning()) throw new Exception("Animation timer cannot be updated while running");
    Supplier<Duration> durationSupplier = keyFrame.getDurationSupplier();
    final Set<PropertyKeyValue<?>> keyValuesSet = keyFrame.getKeyValues();
    if (!keyValuesSet.isEmpty()) {
      final PropertyAnimationHandle handler =
          new PropertyAnimationHandle(
              durationSupplier, keyFrame.getAnimateCondition(), keyFrame.getKeyValues());
      animationHandlers.add(handler);
      mutableFrames.put(keyFrame, handler);
    }
  }

  public void removeKeyFrame(PropertyKeyFrame keyFrame) throws Exception {
    if (isRunning()) throw new Exception("Animation timer cannot be updated while running");
    PropertyAnimationHandle handler = mutableFrames.get(keyFrame);
    animationHandlers.remove(handler);
  }

  public void start() {
    running = true;
    startTime = -1;
    animationHandlers.forEach(PropertyAnimationHandle::init);
    cacheMementos.forEach(PropertyAnimationCacheMemento::cache);
  }

  public void handle(long now) {
    startTime = startTime == -1 ? now : startTime;
    elapsedMillis = (now - startTime) / 1000000.0;
    boolean stop = true;
    for (PropertyAnimationHandle handler : animationHandlers) {
      handler.animate(elapsedMillis);
      if (!handler.getFinished()) {
        stop = false;
      }
    }
    if (stop) {
      this.stop();
    }
  }

  public void reverseAndContinue() {
    if (isRunning()) {
      this.running = false;
      animationHandlers.forEach(handler -> handler.reverse(elapsedMillis));
      startTime = -1;
      this.running = true;
    } else {
      start();
    }
  }

  public void stop() {
    running = false;
    animationHandlers.forEach(PropertyAnimationHandle::clear);
    cacheMementos.forEach(PropertyAnimationCacheMemento::restore);
    if (onFinished != null) {
      onFinished.run();
    }
  }

  public void applyEndValues() {
    if (isRunning()) {
      this.running = false;
    }
    animationHandlers.forEach(PropertyAnimationHandle::applyEndValues);
    startTime = -1;
  }

  public boolean isRunning() {
    return running;
  }

  private Runnable onFinished = null;

  public void setOnFinished(Runnable onFinished) {
    this.onFinished = onFinished;
  }

  public void setCacheNodes(Node... nodesToCache) {
    cacheMementos.clear();
    if (nodesToCache != null) {
      for (Node node : nodesToCache) {
        cacheMementos.add(new PropertyAnimationCacheMemento(node));
      }
    }
  }

  public void dispose() {
    cacheMementos.clear();
    animationHandlers.forEach(PropertyAnimationHandle::dispose);
    animationHandlers.clear();
  }
}
