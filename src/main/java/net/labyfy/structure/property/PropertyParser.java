package net.labyfy.structure.property;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.labyfy.structure.annotation.AnnotationCollector;
import net.labyfy.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.structure.identifier.Identifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class PropertyParser {

  private final AnnotationCollector annotationCollector;

  @Inject
  private PropertyParser(AnnotationCollector annotationCollector) {
    this.annotationCollector = annotationCollector;
  }

  public Property.Base parse(LocatedIdentifiedAnnotation locatedIdentifiedAnnotation) {
    Multimap<Class<? extends Annotation>, LocatedIdentifiedAnnotation> identifiers =
        HashMultimap.create();

    if (locatedIdentifiedAnnotation.getType() == LocatedIdentifiedAnnotation.Type.CLASS) {
      Class clazz = locatedIdentifiedAnnotation.getLocation();

      this.annotationCollector
          .getTransitiveAnnotations(clazz)
          .forEach(
              annotation ->
                  identifiers.put(
                      this.annotationCollector.getRealAnnotationClass(annotation),
                      new LocatedIdentifiedAnnotation(
                          this.annotationCollector
                              .getRealAnnotationClass(annotation)
                              .getDeclaredAnnotation(Identifier.class),
                          annotation,
                          clazz,
                          LocatedIdentifiedAnnotation.Type.CLASS,
                          LocatedIdentifiedAnnotation.Type.CLASS)));

      for (Method method : clazz.getDeclaredMethods()) {
        this.annotationCollector
            .getTransitiveAnnotations(method)
            .forEach(
                annotation ->
                    identifiers.put(
                        this.annotationCollector.getRealAnnotationClass(annotation),
                        new LocatedIdentifiedAnnotation(
                            this.annotationCollector
                                .getRealAnnotationClass(annotation)
                                .getDeclaredAnnotation(Identifier.class),
                            annotation,
                            method,
                            LocatedIdentifiedAnnotation.Type.METHOD,
                            LocatedIdentifiedAnnotation.Type.CLASS)));
      }

    } else if (locatedIdentifiedAnnotation.getType() == LocatedIdentifiedAnnotation.Type.METHOD) {
      Method method = locatedIdentifiedAnnotation.getLocation();

      this.annotationCollector
          .getTransitiveAnnotations(method)
          .forEach(
              annotation ->
                  identifiers.put(
                      this.annotationCollector.getRealAnnotationClass(annotation),
                      new LocatedIdentifiedAnnotation(
                          this.annotationCollector
                              .getRealAnnotationClass(annotation)
                              .getDeclaredAnnotation(Identifier.class),
                          annotation,
                          method,
                          LocatedIdentifiedAnnotation.Type.METHOD,
                          LocatedIdentifiedAnnotation.Type.METHOD)));

      this.annotationCollector
          .getTransitiveAnnotations(method.getDeclaringClass())
          .forEach(
              annotation ->
                  identifiers.put(
                      this.annotationCollector.getRealAnnotationClass(annotation),
                      new LocatedIdentifiedAnnotation(
                          this.annotationCollector
                              .getRealAnnotationClass(annotation)
                              .getDeclaredAnnotation(Identifier.class),
                          annotation,
                          method,
                          LocatedIdentifiedAnnotation.Type.METHOD,
                          LocatedIdentifiedAnnotation.Type.METHOD)));
    }

    Multimap<LocatedIdentifiedAnnotation, Property.Base> subProperties =
        LinkedListMultimap.create();

    for (Property property : locatedIdentifiedAnnotation.getIdentifier().requiredProperties()) {
      if (!identifiers.containsKey(property.value())) {
        System.out.println("Errorrrr " + property.value());
        return null;
      }

      for (LocatedIdentifiedAnnotation identifiedAnnotation : identifiers.get(property.value())) {
        Property.Base parse = this.parse(identifiedAnnotation);
        subProperties.put(identifiedAnnotation, parse);
      }
    }
    return new Property.Base(locatedIdentifiedAnnotation, subProperties.values());
  }
}
