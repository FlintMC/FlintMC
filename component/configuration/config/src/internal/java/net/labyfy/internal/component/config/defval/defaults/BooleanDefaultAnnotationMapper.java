package net.labyfy.internal.component.config.defval.defaults;

import com.google.inject.Singleton;
import net.labyfy.component.config.defval.annotation.DefaultBoolean;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultBoolean.class)
public class BooleanDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultBoolean> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultBoolean annotation) {
    return annotation.value();
  }
}
