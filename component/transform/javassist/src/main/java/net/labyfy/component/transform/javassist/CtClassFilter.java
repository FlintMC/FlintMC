package net.labyfy.component.transform.javassist;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.mappings.DefaultNameResolver;
import net.labyfy.component.commons.resolve.NameResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(requireParent = true)
@Transitive
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CtClassFilter {
  CtClassFilters value();

  String className();

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;
}
