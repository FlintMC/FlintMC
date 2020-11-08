package net.flintmc.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import javassist.CtMethod;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.util.commons.resolve.NameResolver;

import java.util.Collection;
import java.util.function.Predicate;

public interface MethodBasedClassTransformMeta extends ClassTransformMeta {

  /**
   * Retrieves filters.
   *
   * @return Filters.
   */
  Collection<Predicate<CtClass>> getFilters();

  /**
   * Retrieves the transformer method.
   *
   * @return An transformer method.
   */
  CtMethod getTransformMethod();

  /**
   * Retrieves the transformer method.
   *
   * @return the transformer method
   */
  CtClass getTransformClass();

  /**
   * Retrieves the transformers instance
   *
   * @return the transformer instance
   */
  Object getTransformInstance();

  /**
   * Retrieves the annotation meta.
   *
   * @return the annotation meta.
   */
  AnnotationMeta<ClassTransform> getAnnotationMeta();

  /**
   * Retrieves the annotation.
   *
   * @return the annotation
   */
  ClassTransform getAnnotation();

  /**
   * Retrieves the name resolver.
   *
   * @return A name resolver.
   */
  NameResolver getClassNameResolver();

  @AssistedFactory(MethodBasedClassTransformMeta.class)
  interface Factory {
    MethodBasedClassTransformMeta create(@Assisted AnnotationMeta<ClassTransform> annotationMeta);
  }
}
