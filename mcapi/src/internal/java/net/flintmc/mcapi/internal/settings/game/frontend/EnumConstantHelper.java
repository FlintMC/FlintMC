package net.flintmc.mcapi.internal.settings.game.frontend;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;

import java.util.Map;

/** A helper for enumeration constants. */
@Singleton
public class EnumConstantHelper {

  /**
   * Retrieves the constant name of an enumeration using the specified ordinal number.
   *
   * @param type The enumeration class.
   * @param ordinal The ordinal number of the constant.
   * @return The constant name through the given ordinal.
   */
  public String getConstantByOrdinal(Class<?> type, int ordinal) {
    return this.getConstants(type).get(ordinal);
  }

  /**
   * Retrieves a key-value system that stores the ordinal-constant name of the given enumeration
   * class.
   *
   * @param type The enumeration class.
   * @return A key value system with the ordinal numbers and constant names or an empty key value
   *     system.
   */
  public Map<Integer, String> getConstants(Class<?> type) {
    Map<Integer, String> constants = Maps.newHashMap();
    if (!type.isEnum()) {
      return constants;
    }

    for (int i = 0; i < type.getEnumConstants().length; i++) {
      constants.put(i, type.getEnumConstants()[i].toString());
    }

    return constants;
  }

  /**
   * Retrieves the ordinal number by the constant name of the enumeration.
   *
   * @param type The enumeration class.
   * @param value The constant name.
   * @return The ordinal by the given constatn name or {@code -1}.
   */
  public int getOrdinal(Class<?> type, String value) {
    if (!type.isEnum()) return -1;

    for (int i = 0; i < type.getEnumConstants().length; i++) {
      if (type.getEnumConstants()[i].toString().equalsIgnoreCase(value)) {
        return i;
      }
    }
    return -1;
  }
}
