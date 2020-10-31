package net.labyfy.internal.component.settings.options.numeric;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(SliderSetting.class)
public class SliderSettingHandler extends RangedSettingHandler implements SettingHandler<SliderSetting> {

  @Inject
  private SliderSettingHandler(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
    super(serializerFactory, annotationSerializer);
  }

  @Override
  public Object getDefaultValue(SliderSetting annotation, ConfigObjectReference reference) {
    double value = annotation.defaultValue();
    Range range = annotation.value();
    if (super.inRange(range, value)) {
      return value;
    }

    return range.min();
  }

  @Override
  public JsonObject serialize(SliderSetting annotation, RegisteredSetting setting) {
    return super.serialize((Number) setting.getCurrentValue(), annotation.value(), setting);
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, SliderSetting annotation) {
    return super.inRange(annotation.value(), input);
  }
}
