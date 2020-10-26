package net.labyfy.component.stereotype.type;

import com.google.inject.Singleton;

@Singleton
public class DefaultTypeNameResolver implements TypeNameResolver {

  public String resolve(Type type) {
    return !type.reference().equals(Type.TypeDummy.class)
        ? type.reference().getName()
        : type.typeName();
  }
}
