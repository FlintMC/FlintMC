package net.labyfy.component.commons.resolve;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotationResolver<T extends Annotation, K> extends Resolver<T, K>{

  K resolve(T t);
}
