/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.commons.annotation;

import com.google.auto.common.SimpleAnnotationMirror;
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
   * Transitively searches the given type element for an annotation of the given type and returns
   * its {@link AnnotationMirror}.
   *
   * @param typeElement The element to transitively search for the given annotation
   * @param className   The class name of the annotation to find
   * @return The annotation mirror of the found annotation
   * @throws IllegalArgumentException If the given element is not annotated with the requested
   *                                  annotation
   */
  public static AnnotationMirror getTransitiveAnnotationMirror(
      TypeElement typeElement, String className) {
    for (AnnotationMirror m : collectTransitiveAnnotations(typeElement)) {
      if (m.getAnnotationType().toString().equals(className)) {
        return m;
      }
    }

    throw new IllegalArgumentException("Type not annotated with requested annotation");
  }

  /**
   * Searches the given type element for an annotation of the given type and returns its {@link
   * AnnotationMirror}.
   *
   * @param element        The element to search for the given annotation
   * @param annotationType The type of the annotation to find
   * @return The annotation mirror of the found annotation or null
   */
  public static AnnotationMirror getAnnotationMirror(Element element, TypeElement annotationType) {
    for (AnnotationMirror targetMirror : element.getAnnotationMirrors()) {
      if (targetMirror.getAnnotationType().asElement().equals(annotationType)) {
        return SimpleAnnotationMirror.of(annotationType, getElementValuesByName(targetMirror));
      }
    }
    return null;
  }

  public static Map<String, AnnotationValue> getElementValuesByName(
      AnnotationMirror annotationMirror) {
    return annotationMirror.getElementValues().entrySet().stream()
        .collect(
            Collectors.toMap(
                entry -> entry.getKey().getSimpleName().toString(), Map.Entry::getValue));
  }

  /**
   * Retrieves the value of a given annotation mirror and falls back to a default value if not
   * found.
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
   * Transitively collects all annotations from the given type element. Transitively means that the
   * annotations are also searched for their annotations.
   *
   * <p>Example:
   *
   * <blockquote>
   *
   * <pre>
   * {@literal @}interface BaseAnnotation {}
   *
   * {@literal @}BaseAnnotation
   * {@literal @}interface TransitiveAnnotation {}
   *
   * {@literal @}TransitiveAnnotation
   *  class SomeClass {}
   * </pre>
   *
   * </blockquote>
   * <p>
   * Calling {@code collectTransitiveAnnotations} on the {@link TypeElement} of {@code SomeClass}
   * would yield {@code [TransitiveAnnotation, BaseAnnotation]}.
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
      for (AnnotationMirror annotationMirror :
          currentMirror.getAnnotationType().asElement().getAnnotationMirrors()) {
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
                                .toString()))) {
          continue;
        }

        // Add the annotation to be checked and collect its mirror
        queue.add(annotationMirror);
        mirrors.add(annotationMirror);
      }
    }

    return mirrors;
  }

  /**
   * Determines whether a collection of annotation mirrors includes one for a specified annotation
   * type.
   *
   * @param annotationType The name of the annotation type to search for
   * @param mirrors        The list of mirrors to search
   * @return {@code true} if the collection contains a mirror for the specified name, {@code false}
   * otherwise
   */
  public static boolean hasMirrorFor(String annotationType,
      Collection<? extends AnnotationMirror> mirrors) {
    for (AnnotationMirror mirror : mirrors) {
      if (((TypeElement) mirror.getAnnotationType().asElement()).getQualifiedName()
          .contentEquals(annotationType)) {
        return true;
      }
    }

    return false;
  }
}
