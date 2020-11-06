package net.labyfy.internal.component.gamesettings.keybind;

import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.gamesettings.keybind.DefaultKeyBind;

@DefaultAnnotationMapper(DefaultKeyBind.class)
public class DefaultKeyBindAnnotationHandler implements DefaultAnnotationMapperHandler<DefaultKeyBind> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultKeyBind annotation) {
    return annotation.value();
  }
}
