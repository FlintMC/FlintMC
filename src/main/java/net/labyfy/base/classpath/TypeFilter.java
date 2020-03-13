package net.labyfy.base.classpath;

@FunctionalInterface
public interface TypeFilter {

  boolean matches(Class<?> clazz);

}
