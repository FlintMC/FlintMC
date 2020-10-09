package net.labyfy.component.stereotype.identifier;

import java.lang.annotation.Annotation;

public class PropertyMeta {

  private final Class<? extends Annotation> annotationType;
  private final boolean required;
  private final boolean allowMultiple;

  public PropertyMeta(Class<? extends Annotation> annotationType, boolean required, boolean allowMultiple) {
    this.annotationType = annotationType;
    this.required = required;
    this.allowMultiple = allowMultiple;
  }

  public Class<? extends Annotation> getAnnotationType() {
    return annotationType;
  }

  public boolean allowMultiple() {
    return this.allowMultiple;
  }

  public boolean isRequired() {
    return this.required;
  }
}
