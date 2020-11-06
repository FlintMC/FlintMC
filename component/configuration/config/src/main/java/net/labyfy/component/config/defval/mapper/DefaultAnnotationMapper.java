package net.labyfy.component.config.defval.mapper;

import net.labyfy.component.config.defval.annotation.DefaultString;
import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

/**
 * Marks an {@link DefaultAnnotationMapperHandler} to be registered in the {@link DefaultAnnotationMapperRegistry}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface DefaultAnnotationMapper {

  /**
   * The annotation type which can be mapped by the {@link DefaultAnnotationMapperHandler}, for example {@link
   * DefaultString DefaultString.class}.
   *
   * @return The annotation type which will be mapped by the {@link DefaultAnnotationMapperHandler}
   */
  Class<? extends Annotation> value();

}
