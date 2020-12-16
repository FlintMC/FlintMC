package net.flintmc.mcapi.settings.flint;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Resolver to map enum constants to their {@link Field}.
 */
public interface EnumFieldResolver {

  /**
   * Retrieves the field that belongs to the given enum constant.
   *
   * @param value The non-null enum constant
   * @return The non-null field to the given constant
   */
  Field getEnumField(Enum<?> value);

  /**
   * Retrieves all fields that belong to enum constants in the given enum class with the key being
   * the {@link Enum#name() name of the constant} and value the field that belongs to the enum
   * constant.
   *
   * @param enumClass The non-null enum class
   * @return The non-null map with all fields to the given constant
   */
  Map<String, Field> getEnumFields(Class<? extends Enum<?>> enumClass);
}
