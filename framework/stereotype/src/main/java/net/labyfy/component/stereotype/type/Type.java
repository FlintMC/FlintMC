package net.labyfy.component.stereotype.type;

import net.labyfy.component.commons.resolve.AnnotationResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a {@link Class}.
 * Used mostly for bytecode manipulation to not be forced to provide direct class references.
 * Only one of the methods {@link Type#typeName()} or {@link Type#reference()} should be used.
 * If both are provided, {@link Type#typeNameResolver()} should handle it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Type {

  /**
   * @return direct class reference
   */
  Class<?> reference() default TypeDummy.class;

  /**
   * @return Class by name, resolved and eventually modified by {@link Type#typeNameResolver()}
   */
  String typeName() default "";

  /**
   * @return The type name resolver for this {@link Type}
   */
  Class<? extends AnnotationResolver<Type, String>> typeNameResolver() default
      DefaultTypeNameResolver.class;

  final class TypeDummy {
    private TypeDummy() {
    }
  }
}
