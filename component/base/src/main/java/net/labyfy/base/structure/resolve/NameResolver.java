package net.labyfy.base.structure.resolve;

@FunctionalInterface
public interface NameResolver {
  String resolve(String name);
}
