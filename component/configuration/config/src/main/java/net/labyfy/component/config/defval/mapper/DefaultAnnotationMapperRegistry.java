package net.labyfy.component.config.defval.mapper;

import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface DefaultAnnotationMapperRegistry {

  Collection<Class<? extends Annotation>> getAnnotationTypes();

  Object getDefaultValue(ConfigObjectReference reference, Annotation annotation);

}
