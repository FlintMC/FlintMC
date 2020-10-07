package net.labyfy.component.transform.javassist;

import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.mappings.DefaultNameResolver;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transitive
@Identifier
public @interface ClassTransform {

  String[] value() default "";

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;

  String version() default "";

}
