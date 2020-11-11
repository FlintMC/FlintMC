package net.flintmc.transform.hook;

import javassist.CannotCompileException;
import javassist.CtMethod;
import net.flintmc.framework.stereotype.type.DefaultTypeNameResolver;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.processing.autoload.DetectableAnnotation;
import net.flintmc.util.commons.resolve.AnnotationResolver;
import net.flintmc.util.commons.resolve.NameResolver;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.DefaultNameResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows for adding code to methods in compiled classes. The annotated method will then be fired
 * whenever the {@link #executionTime() point} in the {@link #methodName() defined method} in the
 * {@link #className() defined class}. <br>
 * <br>
 * If the annotated method has a return type that is not {@code void}, this doesn't just fire the
 * annotated method, but cancels further execution of this method. However, this does only work if
 * the {@link #methodName() hooked method} doesn't have the return type {@code void} OR the return
 * type of the annotated method is {@link HookResult}.
 *
 * <p>If the result of the annotated method is not {@link HookResult} and not {@code void}, the
 * exact result (which may also be {@code null}) will be returned in the {@link #methodName() hooked
 * method}.
 *
 * <p>If it is {@link HookResult}, see the values in {@link HookResult} for more information about
 * what will happen in the method.
 *
 * @see HookResult
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation(metaData = HookFilter.class)
public @interface Hook {

  /**
   * Retrieves the name of the class where the method to be hooked is located. This will then be
   * mapped by the {@link #classNameResolver()}. By default, this also maps the given name to its
   * obfuscated name with the {@link ClassMappingProvider}.
   *
   * <p>If the given name is empty, the {@link HookFilter} annotation on the same method where this
   * annotation is specified will be used to find the classes where this hook is defined.
   *
   * @return The name of the class where {@link #methodName()} is located
   * @see #methodName()
   * @see #classNameResolver()
   * @see HookFilter
   */
  String className() default "";

  /**
   * Retrieves the name of the method which should be hooked, this has to exist in the defined
   * {@link #className() class}. This will then be mapped by the {@link #methodNameResolver()}.
   *
   * @return The name of the method to be hooked
   * @see #className()
   */
  String methodName();

  /**
   * Retrieves an array of all parameter types from the {@link #methodName() method that should be
   * hooked}. Those will be mapped to the actual type name by the {@link
   * #parameterTypeNameResolver()}.
   *
   * @return The array of parameters of the method to be hooked
   * @see Type
   */
  Type[] parameters() default {};

  /**
   * Retrieves the minecraft version where this hook should be active, for example "1.15.2". If it
   * is empty, it will work in every version.
   *
   * @return The version where this hook should be available
   */
  String version() default "";

  /**
   * Retrieves the times where this hook should be executed, if it is empty, it won't be executed.
   *
   * @return All times where this hook should be executed
   */
  ExecutionTime[] executionTime() default ExecutionTime.AFTER;

  /**
   * Retrieves the class to resolve the {@link #className()}.
   *
   * @return The resolver for the class name
   * @see #className()
   */
  Class<? extends NameResolver> classNameResolver() default DefaultNameResolver.class;

  /**
   * Retrieves the class to resolve the {@link #parameters()}.
   *
   * @return The resolver for the parameter types
   * @see Type
   */
  Class<? extends AnnotationResolver<Type, String>> parameterTypeNameResolver() default
      DefaultTypeNameResolver.class;

  /**
   * Retrieves the class to resolve the {@link #methodName()}.
   *
   * @return The resolver for the method name
   * @see #methodName()
   */
  Class<? extends AnnotationResolver<Hook, String>> methodNameResolver() default
      DefaultMethodNameResolver.class;

  /** Times to call the hook inside of a method. */
  enum ExecutionTime {
    /**
     * This time defines that the hook should be fired before anything else in the hooked method.
     */
    BEFORE {
      public void insert(CtMethod ctMethod, String source) throws CannotCompileException {
        ctMethod.insertBefore(source);
      }
    },

    /** This time defines that the hook should be fired after anything else in the hooked method. */
    AFTER {
      public void insert(CtMethod ctMethod, String source) throws CannotCompileException {
        ctMethod.insertAfter(source);
      }
    };

    /**
     * Inserts the given source code at the position that is defined by this time into the given
     * method.
     *
     * @param ctMethod The non-null method to insert the source code
     * @param source The non-null source code to be inserted
     * @throws CannotCompileException If the given source code cannot be compiled by Javassist
     */
    public abstract void insert(CtMethod ctMethod, String source) throws CannotCompileException;
  }
}
