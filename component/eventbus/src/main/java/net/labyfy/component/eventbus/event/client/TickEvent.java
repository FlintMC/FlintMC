package net.labyfy.component.eventbus.event.client;

import com.google.inject.name.Named;
import net.labyfy.component.eventbus.event.filter.EventGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class TickEvent {

  private final Type type;

  public TickEvent(Type type) {
    this.type = type;
  }

  @Named("type")
  public Type getType() {
    return type;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @EventGroup(groupEvent = TickEvent.class)
  public @interface TickPhase {

    @Named("type")
    Type type() default Type.CLIENT;

  }

  public enum Type {

    CLIENT,
    RENDER,
    WORLD_RENDERER

  }

}
