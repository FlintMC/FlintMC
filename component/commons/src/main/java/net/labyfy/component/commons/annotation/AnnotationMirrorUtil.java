package net.labyfy.component.commons.annotation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.*;

public class AnnotationMirrorUtil {

  public static AnnotationMirror getTransitiveAnnotationMirror(
      TypeElement typeElement, String className) {
    for (AnnotationMirror m : collectTransitiveAnnotations(typeElement)) {
      if (m.getAnnotationType().toString().equals(className)) {
        return m;
      }
    }
    throw new RuntimeException("Type not annotated with requested annotation");
  }

  public static boolean hasAnnotationMirror(TypeElement typeElement, String className) {
    for (AnnotationMirror m : collectTransitiveAnnotations(typeElement)) {
      if (m.getAnnotationType().toString().equals(className)) {
        return true;
      }
    }
    return false;
  }

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

  public static Collection<AnnotationMirror> collectTransitiveAnnotations(TypeElement typeElement) {
    Queue<AnnotationMirror> queue = new LinkedList<>(typeElement.getAnnotationMirrors());
    Collection<AnnotationMirror> mirrors = new HashSet<>(queue);

    while (!queue.isEmpty()) {
      AnnotationMirror poll = queue.poll();
      for (AnnotationMirror annotationMirror :
          poll.getAnnotationType().asElement().getAnnotationMirrors()) {
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
        queue.add(annotationMirror);
        mirrors.add(annotationMirror);
      }
    }

    return mirrors;
  }

  public static Collection<AnnotationMirror> collectTransitiveAnnotations(
      AnnotationMirror annotationMirror) {

    Collection<AnnotationMirror> annotationMirrors = new HashSet<>();

    for (AnnotationMirror mirror :
        annotationMirror.getAnnotationType().asElement().getAnnotationMirrors()) {
      if (((TypeElement) mirror.getAnnotationType().asElement())
          .getQualifiedName()
          .toString()
          .equals(
              ((TypeElement) annotationMirror.getAnnotationType().asElement())
                  .getQualifiedName()
                  .toString())) {
        return Collections.emptyList();
      }
      annotationMirrors.add(mirror);
    }

    for (AnnotationMirror mirror :
        annotationMirror.getAnnotationType().asElement().getAnnotationMirrors()) {
      annotationMirrors.addAll(collectTransitiveAnnotations(mirror));
    }

    return annotationMirrors;
  }
}
