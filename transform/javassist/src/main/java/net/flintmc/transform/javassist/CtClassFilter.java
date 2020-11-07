package net.flintmc.transform.javassist;

import net.flintmc.processing.autoload.DetectableAnnotation;
import net.flintmc.util.commons.resolve.NameResolver;
import net.flintmc.util.mappings.DefaultNameResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@DetectableAnnotation(requiresParent = true)
public @interface CtClassFilter {
  CtClassFilters value();

  String className();

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;
}
