package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultChar;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultChar.class)
public class CharDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultChar> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultChar annotation) {
    return annotation.value();
  }
}
