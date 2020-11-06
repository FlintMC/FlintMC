package net.labyfy.component.config.defval.mapper;

import net.labyfy.component.processing.autoload.DetectableAnnotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface DefaultAnnotationMapper {

  Class<? extends Annotation> value();

}
