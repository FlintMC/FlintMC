package net.labyfy.component.stereotype.type;

import net.labyfy.component.commons.resolve.AnnotationResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Type {

  Class<?> reference() default TypeDummy.class;

  String typeName() default "";

  Class<? extends AnnotationResolver<Type, String>> typeNameResolver() default
      DefaultTypeNameResolver.class;

  final class TypeDummy {
    private TypeDummy() {}
  }
}
