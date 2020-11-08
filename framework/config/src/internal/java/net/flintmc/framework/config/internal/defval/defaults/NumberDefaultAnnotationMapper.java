package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultNumber;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultNumber.class)
public class NumberDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultNumber> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultNumber annotation) {
    return annotation.value();
  }
}
