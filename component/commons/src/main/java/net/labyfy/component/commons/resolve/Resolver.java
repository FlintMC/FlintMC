package net.labyfy.component.commons.resolve;

@FunctionalInterface
public interface Resolver<T, K> {
  K resolve(T t);
}
