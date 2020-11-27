package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultString.class)
public class StringDefaultAnnotationMapper
    implements DefaultAnnotationMapperHandler<DefaultString> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultString annotation) {
    return annotation.value();
  }
}
