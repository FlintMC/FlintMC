package net.flintmc.mcapi.internal.settings.game.keybind;

import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.game.keybind.DefaultKeyBind;

@DefaultAnnotationMapper(DefaultKeyBind.class)
public class DefaultKeyBindAnnotationHandler
    implements DefaultAnnotationMapperHandler<DefaultKeyBind> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultKeyBind annotation) {
    return annotation.value();
  }
}
