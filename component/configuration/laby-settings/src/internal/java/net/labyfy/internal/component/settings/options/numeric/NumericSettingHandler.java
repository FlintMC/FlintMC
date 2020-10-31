package net.labyfy.internal.component.settings.options.numeric;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.numeric.NumericSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(NumericSetting.class)
public class NumericSettingHandler extends RangedSettingHandler implements SettingHandler<NumericSetting> {

  @Inject
  private NumericSettingHandler(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
    super(serializerFactory, annotationSerializer);
  }

  @Override
  public Object getDefaultValue(NumericSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public JsonObject serialize(NumericSetting annotation, RegisteredSetting setting) {
    return super.serialize((Number) setting.getCurrentValue(), annotation.value(), setting);
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, NumericSetting annotation) {
    return super.inRange(annotation.value(), input);
  }
}
