package net.labyfy.base.structure.resolve;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotationResolver<T extends Annotation, K> {

  K resolve(T t);
}
