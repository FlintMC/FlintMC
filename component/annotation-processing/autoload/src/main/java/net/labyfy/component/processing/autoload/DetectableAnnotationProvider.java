package net.labyfy.component.processing.autoload;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface DetectableAnnotationProvider {
  default void register(List<DetectableAnnotationMeta> consumer) {
  }

  class DetectableAnnotationMeta {

    private final ElementKind elementType;
    private final Identifier identifier;
    private final Annotation annotation;
    private final Collection<DetectableAnnotationMeta> metaData;

    public DetectableAnnotationMeta(ElementKind elementType, Identifier identifier, Annotation annotation, DetectableAnnotationMeta... metaData) {
      this.elementType = elementType;
      this.identifier = identifier;
      this.annotation = annotation;
      this.metaData = Arrays.asList(metaData);
    }

    public Annotation getAnnotation() {
      return annotation;
    }

    public ElementKind getElementKind() {
      return elementType;
    }

    public Identifier getIdentifier() {
      return identifier;
    }

    public Collection<DetectableAnnotationMeta> getMetaData() {
      return metaData;
    }

    interface Identifier {
    }

    static class ClassIdentifier implements Identifier {
      private final String name;

      public ClassIdentifier(String name) {
        this.name = name;
      }

      public String getName() {
        return name;
      }
    }

    public static class MethodIdentifier implements Identifier {
      private final String name;
      private final String[] parameters;

      public MethodIdentifier(String name, String... parameters) {
        this.name = name;
        this.parameters = parameters;
      }

      public String[] getParameters() {
        return parameters;
      }

      public String getName() {
        return name;
      }


    }

  }

}
