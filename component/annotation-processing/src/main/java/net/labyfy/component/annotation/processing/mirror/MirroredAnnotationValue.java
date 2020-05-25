package net.labyfy.component.annotation.processing.mirror;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;

class MirroredAnnotationValue {
  private final ExecutableElement key;
  private final AnnotationValue value;

  MirroredAnnotationValue(ExecutableElement key, AnnotationValue value) {
    this.key = key;
    this.value = value;
  }

  public ExecutableElement getKey() {
    return key;
  }

  public AnnotationValue getValue() {
    return value;
  }
}
