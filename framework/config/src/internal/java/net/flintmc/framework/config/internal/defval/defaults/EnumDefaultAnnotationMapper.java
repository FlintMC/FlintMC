package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultEnum;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultEnum.class)
public class EnumDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultEnum> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultEnum annotation) {
    int index = annotation.value();
    return ((Class<?>) reference.getSerializedType()).getEnumConstants()[index];
  }
}
