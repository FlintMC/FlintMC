package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import com.google.gson.JsonObject;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplay;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplays;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

public class RangedSettingHandler {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  protected RangedSettingHandler(
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  protected boolean inRange(Range range, Object value) {
    if (!(value instanceof Number)) {
      return false;
    }

    Number number = (Number) value;
    double d = number.doubleValue();
    int decimals = range.decimals();

    if (decimals == 0 && d != (long) d) {
      return false;
    }

    int decimalLength = String.valueOf(d - (long) d).length() - 2; // 2 = '0.'
    if (decimalLength > decimals) {
      return false;
    }

    return d >= range.min() && d <= range.max();
  }

  protected JsonObject serialize(Number value, Range range, RegisteredSetting setting) {
    JsonObject object = new JsonObject();

    object.addProperty("value", value);

    JsonObject rangeObject = new JsonObject();
    object.add("range", rangeObject);

    if (range.min() != Double.MIN_VALUE) {
      rangeObject.addProperty("min", range.min());
    }
    if (range.max() != Double.MAX_VALUE) {
      rangeObject.addProperty("max", range.max());
    }
    rangeObject.addProperty("decimals", range.decimals());

    NumericDisplays repeatable =
        setting.getReference().findLastAnnotation(NumericDisplays.class);
    if (repeatable != null) {
      JsonObject displays = new JsonObject();
      object.add("displays", displays);

      for (NumericDisplay display : repeatable.value()) {
        displays.add(
            String.valueOf(display.value()),
            this.serializerFactory
                .gson()
                .getGson()
                .toJsonTree(this.annotationSerializer.deserialize(display.display())));
      }
    }

    return object;
  }
}
