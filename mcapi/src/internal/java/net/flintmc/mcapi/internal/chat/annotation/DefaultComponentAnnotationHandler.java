package net.flintmc.mcapi.internal.chat.annotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.annotation.DefaultComponent;

@Singleton
@DefaultAnnotationMapper(DefaultComponent.class)
public class DefaultComponentAnnotationHandler implements DefaultAnnotationMapperHandler<DefaultComponent> {

  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public DefaultComponentAnnotationHandler(ComponentAnnotationSerializer annotationSerializer) {
    this.annotationSerializer = annotationSerializer;
  }

  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultComponent annotation) {
    return this.annotationSerializer.deserialize(annotation.value());
  }
}
