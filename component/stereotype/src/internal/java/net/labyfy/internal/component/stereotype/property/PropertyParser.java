package net.labyfy.internal.component.stereotype.property;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.internal.component.stereotype.annotation.AnnotationCollector;
import net.labyfy.internal.component.stereotype.identifier.DefaultLocatedIdentifiedAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class PropertyParser {

  private PropertyParser() {}

  public static Property.Base parse(LocatedIdentifiedAnnotation locatedIdentifiedAnnotation) {
    Multimap<Class<? extends Annotation>, LocatedIdentifiedAnnotation> identifiers =
        HashMultimap.create();

    if (locatedIdentifiedAnnotation.getType() == LocatedIdentifiedAnnotation.Type.CLASS) {
      Class clazz = locatedIdentifiedAnnotation.getLocation();
      AnnotationCollector
          .getTransitiveAnnotations(clazz)
          .forEach(
              annotation ->
                  identifiers.put(
                      AnnotationCollector.getRealAnnotationClass(annotation),
                      new DefaultLocatedIdentifiedAnnotation(
                          AnnotationCollector.getRealAnnotationClass(annotation)
                              .getDeclaredAnnotation(Identifier.class),
                          annotation,
                          clazz,
                          LocatedIdentifiedAnnotation.Type.CLASS,
                          LocatedIdentifiedAnnotation.Type.CLASS)));

      for (Method method : clazz.getDeclaredMethods()) {
        AnnotationCollector.getTransitiveAnnotations(method)
            .forEach(
                annotation ->
                    identifiers.put(
                        AnnotationCollector.getRealAnnotationClass(annotation),
                        new DefaultLocatedIdentifiedAnnotation(
                            AnnotationCollector.getRealAnnotationClass(annotation)
                                .getDeclaredAnnotation(Identifier.class),
                            annotation,
                            method,
                            LocatedIdentifiedAnnotation.Type.METHOD,
                            LocatedIdentifiedAnnotation.Type.CLASS)));
      }

    } else if (locatedIdentifiedAnnotation.getType() == LocatedIdentifiedAnnotation.Type.METHOD) {
      Method method = locatedIdentifiedAnnotation.getLocation();

      AnnotationCollector.getTransitiveAnnotations(method)
          .forEach(
              annotation ->
                  identifiers.put(
                      AnnotationCollector.getRealAnnotationClass(annotation),
                      new DefaultLocatedIdentifiedAnnotation(
                          AnnotationCollector.getRealAnnotationClass(annotation)
                              .getDeclaredAnnotation(Identifier.class),
                          annotation,
                          method,
                          LocatedIdentifiedAnnotation.Type.METHOD,
                          LocatedIdentifiedAnnotation.Type.METHOD)));

      AnnotationCollector.getTransitiveAnnotations(method.getDeclaringClass())
          .forEach(
              annotation ->
                  identifiers.put(
                      AnnotationCollector.getRealAnnotationClass(annotation),
                      new DefaultLocatedIdentifiedAnnotation(
                          AnnotationCollector.getRealAnnotationClass(annotation)
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
        return null;
      }

      for (LocatedIdentifiedAnnotation identifiedAnnotation : identifiers.get(property.value())) {
        Property.Base parse = parse(identifiedAnnotation);
        subProperties.put(identifiedAnnotation, parse);
      }
    }

    for (Property property : locatedIdentifiedAnnotation.getIdentifier().optionalProperties()) {
      if (!identifiers.containsKey(property.value())) {
        continue;
      }

      for (LocatedIdentifiedAnnotation identifiedAnnotation : identifiers.get(property.value())) {
        Property.Base parse = parse(identifiedAnnotation);
        subProperties.put(identifiedAnnotation, parse);
      }
    }

    return new Property.Base(locatedIdentifiedAnnotation, subProperties.values());
  }
}
