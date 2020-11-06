package net.labyfy.component.config.defval.mapper;

import net.labyfy.component.config.defval.annotation.DefaultString;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * The registry with all {@link DefaultAnnotationMapperHandler}s for mapping an annotation (e.g. {@link DefaultString})
 * to an object which can be serialized by a {@link ConfigSerializationHandler}.
 */
public interface DefaultAnnotationMapperRegistry {

  /**
   * Retrieves an immutable collection of all annotation types that can be serialized in this registry.
   *
   * @return A non-null immutable collection of all annotation types in this registry
   */
  Collection<Class<? extends Annotation>> getAnnotationTypes();

  /**
   * Maps the given annotation to an object that can be serialized by a {@link ConfigSerializationHandler}.
   *
   * @param reference  The non-null reference that contains the given annotation
   * @param annotation The non-null annotation to be mapped
   * @return The mapped object from the annotation, can be {@code null}
   */
  Object getDefaultValue(ConfigObjectReference reference, Annotation annotation);

}
