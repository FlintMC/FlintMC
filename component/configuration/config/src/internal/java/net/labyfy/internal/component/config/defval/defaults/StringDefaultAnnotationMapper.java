package net.labyfy.internal.component.config.defval.defaults;

import com.google.inject.Singleton;
import net.labyfy.component.config.defval.annotation.DefaultString;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultString.class)
public class StringDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultString> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultString annotation) {
    return annotation.value();
  }
}
