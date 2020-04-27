package net.labyfy.base.structure.representation;

import net.labyfy.base.structure.resolve.AnnotationResolver;

import javax.inject.Singleton;

@Singleton
public class DefaultTypeNameResolver implements AnnotationResolver<Type, String> {

  public String resolve(Type type) {
    return !type.reference().equals(Type.TypeDummy.class)
        ? type.reference().getName()
        : type.typeName();
  }
}
