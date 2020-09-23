package net.labyfy.component.eventbus.event.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class TickEvent {

  private final Type type;

  public TickEvent(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface TickPhase {

    Type type() default Type.CLIENT;

  }

  public enum Type {

    CLIENT,
    RENDER,
    WORLD_RENDERER

  }

}
