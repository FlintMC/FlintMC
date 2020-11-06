package net.labyfy.component.config.defval.mapper;

import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;

public interface DefaultAnnotationMapperHandler<A extends Annotation> {

  Object getDefaultValue(ConfigObjectReference reference, A annotation);

}
