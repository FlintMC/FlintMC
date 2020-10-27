package net.flintmc.framework.stereotype.type;

import net.flintmc.util.commons.resolve.AnnotationResolver;

/**
 * Resolves a class name from {@link Type}.
 * Could be used for example to create shortcuts or always return the obfuscated name of {@link Type}.
 */
@FunctionalInterface
public interface TypeNameResolver extends AnnotationResolver<Type, String> {

  String resolve(Type type);
}
