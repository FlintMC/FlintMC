package net.labyfy.internal.component.config.defval.defaults;

import com.google.inject.Singleton;
import net.labyfy.component.config.defval.annotation.DefaultChar;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultChar.class)
public class CharDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultChar> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultChar annotation) {
    return annotation.value();
  }
}
