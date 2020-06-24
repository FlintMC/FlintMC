package net.labyfy.component.stereotype.type;

import net.labyfy.component.commons.resolve.AnnotationResolver;

/**
 * Resolves a class name from {@link Type}.
 * Could be used for example to create shortcuts or always return the obfuscated name of {@link Type}.
 */
@FunctionalInterface
public interface TypeNameResolver extends AnnotationResolver<Type, String> {

  String resolve(Type type);
}
