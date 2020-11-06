package net.labyfy.internal.component.config.defval.defaults;

import com.google.inject.Singleton;
import net.labyfy.component.config.defval.annotation.DefaultEnum;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultEnum.class)
public class EnumDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultEnum> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultEnum annotation) {
    int index = annotation.value();
    return ((Class<?>) reference.getSerializedType()).getEnumConstants()[index];
  }
}
