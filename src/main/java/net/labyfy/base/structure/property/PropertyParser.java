package net.labyfy.base.structure.property;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.identifier.Identifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Singleton
public class PropertyParser {

  @Inject
  private PropertyParser() {}

  public Property.Base parse(LocatedIdentifiedAnnotation locatedIdentifiedAnnotation) {
    Multimap<Class<? extends Annotation>, LocatedIdentifiedAnnotation> identifiers =
        HashMultimap.create();

    if (locatedIdentifiedAnnotation.getType() == LocatedIdentifiedAnnotation.Type.CLASS) {
      Class clazz = locatedIdentifiedAnnotation.getLocation();
      AnnotationCollector.getTransitiveAnnotations(clazz)
          .forEach(
              annotation ->
                  identifiers.put(
                      AnnotationCollector.getRealAnnotationClass(annotation),
                      new LocatedIdentifiedAnnotation(
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
                        new LocatedIdentifiedAnnotation(
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
                      new LocatedIdentifiedAnnotation(
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
                      new LocatedIdentifiedAnnotation(
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
