package net.labyfy.base.structure.representation;

import net.labyfy.base.structure.resolve.AnnotationResolver;

import java.lang.annotation.Annotation;

public class Types {

  private Types() {}

  public static Type of(String typeName) {
    return new Type() {
      public Class<?> reference() {
        return TypeDummy.class;
      }

      public String typeName() {
        return typeName;
      }

      public Class<? extends AnnotationResolver<Type, String>> typeNameResolver() {
        return DefaultTypeNameResolver.class;
      }

      public Class<? extends Annotation> annotationType() {
        return Type.class;
      }

    };
  }

  public static Type of(Class<?> reference) {
    return new Type() {
      public Class<?> reference() {
        return reference;
      }

      public String typeName() {
        return "";
      }

      public Class<? extends AnnotationResolver<Type, String>> typeNameResolver() {
        return DefaultTypeNameResolver.class;
      }

      public Class<? extends Annotation> annotationType() {
        return Type.class;
      }
    };
  }
}
