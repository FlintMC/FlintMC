package net.labyfy.base.structure.resolve;

import com.google.inject.Singleton;

@Singleton
public class DefaultNameResolver implements NameResolver {
  public String resolve(String name) {
    return name;
  }
}
