package net.labyfy.component.commons.resolve;

@FunctionalInterface
public interface NameResolver extends Resolver<String, String> {
  String resolve(String name);
}
