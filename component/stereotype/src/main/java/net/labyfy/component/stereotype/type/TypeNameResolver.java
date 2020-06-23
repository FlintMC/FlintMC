package net.labyfy.component.stereotype.type;

import net.labyfy.component.commons.resolve.AnnotationResolver;

@FunctionalInterface
public interface TypeNameResolver extends AnnotationResolver<Type, String> {
  String resolve(Type type);
}
