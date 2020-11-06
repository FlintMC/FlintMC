package net.labyfy.internal.component.settings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.settings.EnumFieldResolver;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(EnumFieldResolver.class)
public class DefaultEnumFieldResolver implements EnumFieldResolver {

  private final Logger logger;
  private final Map<Class<?>, Map<String, Field>> cachedFields;

  @Inject
  private DefaultEnumFieldResolver(@InjectLogger Logger logger) {
    this.logger = logger;
    this.cachedFields = new HashMap<>();
  }

  @Override
  public Field getEnumField(Enum<?> value) {
    return this.getEnumFields(value.getDeclaringClass()).get(value.name());
  }

  @Override
  public Map<String, Field> getEnumFields(Class<? extends Enum<?>> enumClass) {
    if (this.cachedFields.containsKey(enumClass)) {
      return this.cachedFields.get(enumClass);
    }

    Map<String, Field> fields = new HashMap<>();

    for (Enum<?> constant : enumClass.getEnumConstants()) {
      try {
        fields.put(constant.name(), enumClass.getDeclaredField(constant.name()));
      } catch (NoSuchFieldException e) {
        this.logger.error("Failed to find enum constant field in "
            + enumClass.getName() + ": " + constant.name(), e);
      }
    }

    Map<String, Field> unmodifiable = Collections.unmodifiableMap(fields);
    this.cachedFields.put(enumClass, unmodifiable);
    return unmodifiable;
  }
}
