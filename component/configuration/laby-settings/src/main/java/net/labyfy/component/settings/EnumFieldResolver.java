package net.labyfy.component.settings;

import java.lang.reflect.Field;
import java.util.Map;

public interface EnumFieldResolver {

  Field getEnumField(Enum<?> value);

  Map<String, Field> getEnumFields(Class<? extends Enum<?>> enumClass);

}
