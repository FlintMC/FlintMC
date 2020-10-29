package net.labyfy.internal.component.settings.options.numeric;

import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.options.numeric.NumericRestriction;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.stereotype.PrimitiveTypeLoader;

import java.lang.reflect.Type;

public class RangedSettingHandler {

  protected boolean inRange(ConfigObjectReference reference, Range range, Object value) {
    if (!(value instanceof Number)) {
      return false;
    }

    Number number = (Number) value;
    Type type = reference.getSerializedType();
    double d = number.doubleValue();

    if (type instanceof Class && ((Class<?>) type).isPrimitive()) {
      type = PrimitiveTypeLoader.getPrimitiveClass((Class<?>) type);
    }

    if (!this.hasRestriction(range, NumericRestriction.ALLOW_DECIMALS) && d != (long) d) {
      return false;
    }

    return d >= range.min() && d <= range.max();
  }

  protected boolean hasRestriction(Range range, NumericRestriction filter) {
    for (NumericRestriction restriction : range.value()) {
      if (restriction == filter) {
        return true;
      }
    }

    return false;
  }

}
