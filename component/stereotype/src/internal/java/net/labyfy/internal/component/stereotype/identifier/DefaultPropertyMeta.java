package net.labyfy.internal.component.stereotype.identifier;

import net.labyfy.component.stereotype.identifier.PropertyMeta;

import java.lang.annotation.Annotation;

public class DefaultPropertyMeta implements PropertyMeta {

  private final Class<? extends Annotation> annotationType;
  private final boolean required;
  private final boolean allowMultiple;

  public DefaultPropertyMeta(Class<? extends Annotation> annotationType, boolean required, boolean allowMultiple) {
    this.annotationType = annotationType;
    this.required = required;
    this.allowMultiple = allowMultiple;
  }

  @Override
  public Class<? extends Annotation> getAnnotationType() {
    return annotationType;
  }

  @Override
  public boolean allowMultiple() {
    return this.allowMultiple;
  }

  @Override
  public boolean isRequired() {
    return this.required;
  }
}
