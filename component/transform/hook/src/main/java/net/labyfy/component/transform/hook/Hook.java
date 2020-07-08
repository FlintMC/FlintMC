package net.labyfy.component.transform.hook;

import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.component.commons.resolve.AnnotationResolver;
import net.labyfy.component.commons.resolve.NameResolver;
import net.labyfy.component.mappings.DefaultNameResolver;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.property.Property;
import net.labyfy.component.stereotype.type.DefaultTypeNameResolver;
import net.labyfy.component.stereotype.type.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Transitive
@Identifier(optionalProperties = @Property(HookFilter.class))
@Deprecated
public @interface Hook {

  String className() default "";

  String methodName();

  Type[] parameters() default {};

  String version() default "";

  ExecutionTime[] executionTime() default ExecutionTime.AFTER;

  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;

  Class<? extends AnnotationResolver<Type, String>> parameterTypeNameResolver() default
      DefaultTypeNameResolver.class;

  Class<? extends AnnotationResolver<Hook, String>> methodNameResolver() default
      DefaultMethodNameResolver.class;

  enum ExecutionTime {
    BEFORE {
      public void insert(CtMethod ctMethod, String source) throws CannotCompileException {
        ctMethod.insertBefore(source);
      }
    },
    AFTER {
      public void insert(CtMethod ctMethod, String source) throws CannotCompileException {
        ctMethod.insertAfter(source);
      }
    };

    public abstract void insert(CtMethod ctMethod, String source) throws CannotCompileException;
  }
}
