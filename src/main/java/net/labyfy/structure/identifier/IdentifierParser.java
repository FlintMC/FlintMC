package net.labyfy.structure.identifier;

import com.google.common.collect.*;
import net.labyfy.structure.annotation.AnnotationCollector;
import net.labyfy.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.structure.property.Property;
import net.labyfy.structure.property.PropertyParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class IdentifierParser {

  private final AnnotationCollector annotationCollector;
  private final PropertyParser propertyParser;

  @Inject
  private IdentifierParser(AnnotationCollector annotationCollector, PropertyParser propertyParser) {
    this.annotationCollector = annotationCollector;
    this.propertyParser = propertyParser;
  }

  public Collection<Identifier.Base> parse(Class<?> clazz) {
    Collection<LocatedIdentifiedAnnotation> standaloneIdentifiers =
        Streams.concat(
                this.findStandaloneClassIdentifiers(clazz),
                Arrays.stream(clazz.getDeclaredMethods())
                    .flatMap(this::findStandaloneMethodIdentifiers))
            .collect(Collectors.toSet());

    Collection<Identifier.Base> identifiers = new LinkedList<>();

    for (LocatedIdentifiedAnnotation standaloneIdentifier : standaloneIdentifiers) {
      Property.Base parse = this.propertyParser.parse(standaloneIdentifier);
      if (parse != null) identifiers.add(new Identifier.Base(standaloneIdentifier, parse));
    }

    return identifiers;
  }

  private Stream<LocatedIdentifiedAnnotation> findStandaloneMethodIdentifiers(Method method) {
    Predicate<Annotation> standalone =
        identifierCandidate ->
            annotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && ((Identifier) identifierCandidate).parents().length == 0;

    return this.annotationCollector.getTransitiveAnnotations(method).stream()
        .filter(
            annotation ->
                annotationCollector
                    .getTransitiveAnnotations(
                        annotationCollector.getRealAnnotationClass(annotation))
                    .stream()
                    .anyMatch(standalone))
        .map(
            annotation ->
                new LocatedIdentifiedAnnotation(
                    (Identifier)
                        annotationCollector
                            .getTransitiveAnnotations(
                                annotationCollector.getRealAnnotationClass(annotation))
                            .stream()
                            .findAny()
                            .orElse(null),
                    annotation,
                    method,
                    LocatedIdentifiedAnnotation.Type.METHOD,
                    LocatedIdentifiedAnnotation.Type.METHOD));
  }

  private Stream<LocatedIdentifiedAnnotation> findStandaloneClassIdentifiers(Class<?> clazz) {
    Predicate<Annotation> standalone =
        identifierCandidate ->
            annotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && ((Identifier) identifierCandidate).parents().length == 0;

    return this.annotationCollector.getTransitiveAnnotations(clazz).stream()
        .filter(
            annotation ->
                annotationCollector
                    .getTransitiveAnnotations(
                        annotationCollector.getRealAnnotationClass(annotation))
                    .stream()
                    .anyMatch(standalone))
        .map(
            annotation ->
                new LocatedIdentifiedAnnotation(
                    (Identifier)
                        annotationCollector
                            .getTransitiveAnnotations(
                                annotationCollector.getRealAnnotationClass(annotation))
                            .stream()
                            .findAny()
                            .orElse(null),
                    annotation,
                    clazz,
                    LocatedIdentifiedAnnotation.Type.CLASS,
                    LocatedIdentifiedAnnotation.Type.CLASS));
  }
}
