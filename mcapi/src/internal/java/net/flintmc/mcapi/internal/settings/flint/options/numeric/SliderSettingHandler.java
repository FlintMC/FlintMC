package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(SliderSetting.class)
public class SliderSettingHandler extends RangedSettingHandler implements SettingHandler<SliderSetting> {

  @Inject
  private SliderSettingHandler(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
    super(serializerFactory, annotationSerializer);
  }

  @Override
  public JsonObject serialize(SliderSetting annotation, RegisteredSetting setting, Object currentValue) {
    return super.serialize(currentValue == null ? 0 : (Number) currentValue, annotation.value(), setting);
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, SliderSetting annotation) {
    return super.inRange(annotation.value(), input);
  }
}
