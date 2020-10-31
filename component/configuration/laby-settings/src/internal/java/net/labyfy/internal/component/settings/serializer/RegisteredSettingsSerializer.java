package net.labyfy.internal.component.settings.serializer;

import net.labyfy.component.settings.serializer.SettingsSerializationHandler;

import java.lang.annotation.Annotation;

public class RegisteredSettingsSerializer<A extends Annotation> {

  private final Class<A> annotationType;
  private final SettingsSerializationHandler<A> handler;

  public RegisteredSettingsSerializer(Class<A> annotationType, SettingsSerializationHandler<A> handler) {
    this.annotationType = annotationType;
    this.handler = handler;
  }

  public Class<A> getAnnotationType() {
    return this.annotationType;
  }

  public SettingsSerializationHandler<A> getHandler() {
    return this.handler;
  }
}
