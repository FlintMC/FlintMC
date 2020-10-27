package net.flintmc.transform.javassist;

import net.flintmc.util.commons.resolve.NameResolver;
import net.flintmc.util.mappings.DefaultNameResolver;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation(metaData = {CtClassFilter.class})
public @interface ClassTransform {

  String[] value() default "";

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;

  String version() default "";

  int priority() default 0;
}
