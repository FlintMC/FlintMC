package net.flintmc.framework.config.defval.mapper;

import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;

import java.lang.annotation.Annotation;

/**
 * The handler for mapping an annotation (e.g. {@link DefaultString}) to an object which can be serialized by a {@link
 * ConfigSerializationHandler}.
 *
 * @param <A> The type of the annotation that can be mapped
 */
public interface DefaultAnnotationMapperHandler<A extends Annotation> {

  /**
   * Maps the given annotation to an object that can be serialized by a {@link ConfigSerializationHandler}.
   *
   * @param reference  The non-null reference that contains the given annotation
   * @param annotation The non-null annotation to be mapped
   * @return The mapped object from the annotation, can be {@code null}
   */
  Object getDefaultValue(ConfigObjectReference reference, A annotation);

}
