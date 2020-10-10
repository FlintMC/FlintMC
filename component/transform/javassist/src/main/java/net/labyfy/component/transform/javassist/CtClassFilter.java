package net.labyfy.component.transform.javassist;

import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.mappings.DefaultNameResolver;
import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@DetectableAnnotation
public @interface CtClassFilter {
  CtClassFilters value();

  String className();

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;

}
