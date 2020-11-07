package net.labyfy.internal.component.settings.options.text;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.DefaultComponent;

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
