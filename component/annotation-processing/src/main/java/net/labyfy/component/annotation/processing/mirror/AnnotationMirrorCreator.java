package net.labyfy.component.annotation.processing.mirror;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationMirrorCreator {
  private final Map<String, Collection<AnnotationMirror>> mirrors;

  private AnnotationMirrorCreator(Map<String, Collection<AnnotationMirror>> mirrors) {
    this.mirrors = mirrors;
  }

  public static AnnotationMirrorCreator of(TypeElement element) {
    Map<String, Collection<AnnotationMirror>> mirrors = new HashMap<>();

    for(AnnotationMirror mirror : element.getAnnotationMirrors()) {
      String name = mirror.getAnnotationType().toString();

      Collection<AnnotationMirror> namedMirrors;
      if(mirrors.containsKey(name)) {
        namedMirrors = mirrors.get(name);
      } else {
        namedMirrors = new HashSet<>();
        mirrors.put(name, namedMirrors);
      }

      namedMirrors.add(mirror);
    }

    return new AnnotationMirrorCreator(mirrors);
  }

  public int count(String annotationClass) {
    return mirrors.containsKey(annotationClass) ? mirrors.get(annotationClass).size() : 0;
  }

  public boolean has(String annotationClass) {
    return count(annotationClass) > 0;
  }

  public MirroredAnnotation get(String annotationClass) {
    if(count(annotationClass) != 1) {
      throw new IllegalStateException("`get` requires exactly one of the requested annotations to be present");
    }

    return MirroredAnnotation.mirror(mirrors.get(annotationClass).iterator().next());
  }

  public Collection<MirroredAnnotation> getAll(String annotationClass) {
    return mirrors.get(annotationClass).stream().map(MirroredAnnotation::mirror).collect(Collectors.toSet());
  }
}
