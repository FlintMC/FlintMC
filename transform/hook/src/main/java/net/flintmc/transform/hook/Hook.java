package net.flintmc.transform.hook;

import javassist.CannotCompileException;
import javassist.CtMethod;
import net.flintmc.util.commons.resolve.AnnotationResolver;
import net.flintmc.util.commons.resolve.NameResolver;
import net.flintmc.util.mappings.DefaultNameResolver;
import net.flintmc.processing.autoload.DetectableAnnotation;
import net.flintmc.framework.stereotype.type.DefaultTypeNameResolver;
import net.flintmc.framework.stereotype.type.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation(metaData = HookFilter.class)
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
