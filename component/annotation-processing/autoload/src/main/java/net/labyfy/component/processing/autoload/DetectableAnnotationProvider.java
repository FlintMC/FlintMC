package net.labyfy.component.processing.autoload;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.List;

public interface DetectableAnnotationProvider {
  default void registerAutoFound(List<DetectableAnnotationMeta> consumer) {
  }

  class DetectableAnnotationMeta {

    private final ElementKind elementType;
    private final String identifier;
    private final Annotation annotation;

    public DetectableAnnotationMeta(ElementKind elementType, String identifier, Annotation annotation) {
      this.elementType = elementType;
      this.identifier = identifier;
      this.annotation = annotation;
    }

    public Annotation getAnnotation() {
      return annotation;
    }

    public ElementKind getElementKind() {
      return elementType;
    }

    public Object getIdentifier() {
      return identifier;
    }
  }

}
