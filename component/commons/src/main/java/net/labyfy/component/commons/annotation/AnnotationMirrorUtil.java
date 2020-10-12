package net.labyfy.component.commons.annotation;

import javax.lang.model.element.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for working with annotations.
 */
public class AnnotationMirrorUtil {
  private AnnotationMirrorUtil() {
  }

  /**
   * Transitively searches the given type element for an annotation of the given
   * type and returns its {@link AnnotationMirror}.
   *
   * @param typeElement The element to transitively search for the given annotation
   * @param className   The class name of the annotation to find
   * @return The annotation mirror of the found annotation
   * @throws IllegalArgumentException If the given element is not annotated with the requested annotation
   */
  public static AnnotationMirror getTransitiveAnnotationMirror(TypeElement typeElement, String className) {
    for (AnnotationMirror m : collectTransitiveAnnotations(typeElement)) {
      if (m.getAnnotationType().toString().equals(className)) {
        return m;
      }
    }

    throw new IllegalArgumentException("Type not annotated with requested annotation");
  }

  /**
   * Searches the given type element for an annotation of the given
   * type and returns its {@link AnnotationMirror}.
   *
   * @param element   The element to search for the given annotation
   * @param className The class name of the annotation to find
   * @return The annotation mirror of the found annotation or null
   */
  public static AnnotationMirror getAnnotationMirror(Element element, String className) {
    for (AnnotationMirror m : element.getAnnotationMirrors()) {
      if (m.getAnnotationType().toString().equals(className)) {
        return m;
      }
    }
    return null;
  }

  public static Map<String, AnnotationValue> getElementValuesByName(AnnotationMirror annotationMirror) {
    return annotationMirror.getElementValues().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue));
  }

  /**
   * Retrieves the value of a given annotation mirror and falls back to a default
   * value if not found.
   *
   * @param annotationMirror The annotation mirror to retrieve the value from
   * @param key              The key to use to retrieve the value from the annotation mirror
   * @param defaultValue     The value to return if the given key does not exist
   * @return The found value or the default value if the requested value was not found
   */
  public static AnnotationValue getAnnotationValue(
      AnnotationMirror annotationMirror, String key, AnnotationValue defaultValue) {
    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
        annotationMirror.getElementValues().entrySet()) {
      if (entry.getKey().getSimpleName().toString().equals(key)) {
        return entry.getValue();
      }
    }

    return defaultValue;
  }

  /**
   * <p>
   * Transitively collects all annotations from the given type element. Transitively means that the annotations
   * are also searched for their annotations.
   * </p>
   *
   * <p>
   * Example:
   * <blockquote><pre>
   * {@literal @}interface BaseAnnotation {}
   *
   * {@literal @}BaseAnnotation
   * {@literal @}interface TransitiveAnnotation {}
   *
   * {@literal @}TransitiveAnnotation
   *  class SomeClass {}
   * </pre></blockquote>
   * Calling {@code collectTransitiveAnnotations} on the {@link TypeElement} of {@code SomeClass} would
   * yield {@code [TransitiveAnnotation, BaseAnnotation]}.
   *
   * @param typeElement The type element to collect annotations transitively from
   * @return A collection of all transitively found annotations
   */
  public static Collection<AnnotationMirror> collectTransitiveAnnotations(TypeElement typeElement) {
    Queue<AnnotationMirror> queue = new LinkedList<>(typeElement.getAnnotationMirrors());
    Collection<AnnotationMirror> mirrors = new HashSet<>(queue);

    while (!queue.isEmpty()) {
      // We still have elements to search
      AnnotationMirror currentMirror = queue.poll();
      for (AnnotationMirror annotationMirror : currentMirror.getAnnotationType().asElement().getAnnotationMirrors()) {
        // Avoid self duplicates
        if (mirrors.stream()
            .anyMatch(
                target ->
                    ((TypeElement) target.getAnnotationType().asElement())
                        .getQualifiedName()
                        .toString()
                        .equals(
                            ((TypeElement) annotationMirror.getAnnotationType().asElement())
                                .getQualifiedName()
                                .toString()))) continue;

        // Add the annotation to be checked and collect its mirror
        queue.add(annotationMirror);
        mirrors.add(annotationMirror);
      }
    }

    return mirrors;
  }
}
