package net.labyfy.internal.component.settings.options.numeric;

import com.google.gson.JsonObject;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.display.NumericDisplay;
import net.labyfy.component.settings.options.numeric.display.RepeatableNumericDisplay;
import net.labyfy.component.settings.registered.RegisteredSetting;

public class RangedSettingHandler {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  protected RangedSettingHandler(ComponentSerializer.Factory serializerFactory, ComponentAnnotationSerializer annotationSerializer) {
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

    RepeatableNumericDisplay repeatable = setting.getReference().findLastAnnotation(RepeatableNumericDisplay.class);
    if (repeatable != null) {
      JsonObject displays = new JsonObject();
      object.add("displays", displays);

      for (NumericDisplay display : repeatable.value()) {
        displays.add(String.valueOf(display.value()),
            this.serializerFactory.gson().getGson().toJsonTree(this.annotationSerializer.deserialize(display.display()))
        );
      }
    }

    return object;
  }

}
