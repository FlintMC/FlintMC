package net.labyfy.base.structure.identifier;

import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.property.PropertyParser;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    Collection<LocatedIdentifiedAnnotation> standaloneIdentifiers =
        new ArrayList<>(findStandaloneClassIdentifiers(clazz));

    for (Method declaredMethod : clazz.getDeclaredMethods()) {
      declaredMethod.setAccessible(true);
      standaloneIdentifiers.addAll(findStandaloneMethodIdentifiers(declaredMethod));
    }

    Collection<Identifier.Base> identifiers = new LinkedList<>();

    for (LocatedIdentifiedAnnotation standaloneIdentifier : standaloneIdentifiers) {
      Property.Base parse = PropertyParser.parse(standaloneIdentifier);
      if (parse != null) identifiers.add(new Identifier.Base(parse));
    }

    return identifiers;
  }

  private static Collection<LocatedIdentifiedAnnotation> findStandaloneMethodIdentifiers(Method method) {

    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && !((Identifier) identifierCandidate).requireParent();

    Collection<LocatedIdentifiedAnnotation> identifiedAnnotations = new ArrayList<>();

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
                    new LocatedIdentifiedAnnotation(
                        (Identifier) annotation,
                        transitiveAnnotation,
                        method,
                        LocatedIdentifiedAnnotation.Type.METHOD,
                        LocatedIdentifiedAnnotation.Type.METHOD))
            .forEach(e -> identifiedAnnotations.add(e));
      }
    }

    return identifiedAnnotations;
  }

  private static Collection<LocatedIdentifiedAnnotation> findStandaloneClassIdentifiers(Class<?> clazz) {
    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && !((Identifier) identifierCandidate).requireParent();

    Collection<LocatedIdentifiedAnnotation> identifiedAnnotations = new HashSet<>();

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
                    new LocatedIdentifiedAnnotation(
                        (Identifier) annotation,
                        transitiveAnnotation,
                        clazz,
                        LocatedIdentifiedAnnotation.Type.CLASS,
                        LocatedIdentifiedAnnotation.Type.CLASS))
            .forEach(identifiedAnnotations::add);
      }
    }

    return identifiedAnnotations;
  }
}
