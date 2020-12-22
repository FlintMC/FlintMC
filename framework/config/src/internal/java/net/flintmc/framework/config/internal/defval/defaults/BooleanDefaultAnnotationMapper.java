package net.flintmc.framework.config.internal.defval.defaults;

import com.google.inject.Singleton;
import net.flintmc.framework.config.defval.annotation.DefaultBoolean;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultBoolean.class)
public class BooleanDefaultAnnotationMapper
    implements DefaultAnnotationMapperHandler<DefaultBoolean> {
  /** {@inheritDoc} */
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultBoolean annotation) {
    return annotation.value();
  }
}
