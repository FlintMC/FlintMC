package net.labyfy.component.transform.javassist;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.resolve.NameResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier
@Transitive
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CtClassFilter {
  CtClassFilters value();

  String className();

  Class<? extends NameResolver> classNameResolver();
}
