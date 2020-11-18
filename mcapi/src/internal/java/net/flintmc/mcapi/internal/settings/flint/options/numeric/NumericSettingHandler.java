package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.numeric.NumericSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(NumericSetting.class)
public class NumericSettingHandler extends RangedSettingHandler
    implements SettingHandler<NumericSetting> {

  @Inject
  private NumericSettingHandler(
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    super(serializerFactory, annotationSerializer);
  }

  @Override
  public JsonObject serialize(
      NumericSetting annotation, RegisteredSetting setting, Object currentValue) {
    return super.serialize(
        currentValue == null ? 0 : (Number) currentValue, annotation.value(), setting);
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, NumericSetting annotation) {
    return super.inRange(annotation.value(), input);
  }
}
