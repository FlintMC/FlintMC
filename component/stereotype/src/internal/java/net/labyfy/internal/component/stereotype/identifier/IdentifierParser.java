package net.labyfy.internal.component.stereotype.identifier;

import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.internal.component.stereotype.annotation.AnnotationCollector;
import net.labyfy.internal.component.stereotype.property.PropertyParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Predicate;

public class IdentifierParser {


  private IdentifierParser() {
  }

  public static Collection<Identifier.Base> parse(Class<?> clazz) {

    Collection<DefaultLocatedIdentifiedAnnotation> standaloneIdentifiers =
        new ArrayList<>(findStandaloneClassIdentifiers(clazz));

    for (Method declaredMethod : clazz.getDeclaredMethods()) {
      declaredMethod.setAccessible(true);
      standaloneIdentifiers.addAll(findStandaloneMethodIdentifiers(declaredMethod));
    }

    Collection<Identifier.Base> identifiers = new LinkedList<>();

    for (DefaultLocatedIdentifiedAnnotation standaloneIdentifier : standaloneIdentifiers) {
      Property.Base parse = PropertyParser.parse(standaloneIdentifier);
      if (parse != null) identifiers.add(new Identifier.Base(parse));
    }

    return identifiers;
  }

  private static Collection<DefaultLocatedIdentifiedAnnotation> findStandaloneMethodIdentifiers(Method method) {

    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && !((Identifier) identifierCandidate).requireParent();

    Collection<DefaultLocatedIdentifiedAnnotation> identifiedAnnotations = new ArrayList<>();

    for (Annotation transitiveAnnotation : AnnotationCollector.getTransitiveAnnotations(method)) {
      boolean found = false;
      for (Annotation annotation :
          AnnotationCollector.getTransitiveAnnotations(
              AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))) {
        if (!standalone.test(annotation)) continue;
        found = true;
      }

      if (found) {
        AnnotationCollector.getTransitiveAnnotations(
            AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))
            .stream()
            .map(
                annotation ->
                    new DefaultLocatedIdentifiedAnnotation(
                        (Identifier) annotation,
                        transitiveAnnotation,
                        method,
                        DefaultLocatedIdentifiedAnnotation.Type.METHOD,
                        DefaultLocatedIdentifiedAnnotation.Type.METHOD))
            .forEach(e -> identifiedAnnotations.add(e));
      }
    }

    return identifiedAnnotations;
  }

  private static Collection<DefaultLocatedIdentifiedAnnotation> findStandaloneClassIdentifiers(Class<?> clazz) {
    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && !((Identifier) identifierCandidate).requireParent();

    Collection<DefaultLocatedIdentifiedAnnotation> identifiedAnnotations = new HashSet<>();

    for (Annotation transitiveAnnotation : AnnotationCollector.getTransitiveAnnotations(clazz)) {
      boolean found = false;
      for (Annotation annotation :
          AnnotationCollector.getTransitiveAnnotations(
              AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))) {
        if (!standalone.test(annotation)) continue;
        found = true;
      }
      if (found) {
        AnnotationCollector.getTransitiveAnnotations(
            AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))
            .stream()
            .map(
                annotation ->
                    new DefaultLocatedIdentifiedAnnotation(
                        (Identifier) annotation,
                        transitiveAnnotation,
                        clazz,
                        DefaultLocatedIdentifiedAnnotation.Type.CLASS,
                        DefaultLocatedIdentifiedAnnotation.Type.CLASS))
            .forEach(identifiedAnnotations::add);
      }
    }

    return identifiedAnnotations;
  }
}
