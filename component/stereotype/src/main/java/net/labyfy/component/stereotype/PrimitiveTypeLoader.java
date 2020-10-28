package net.labyfy.component.stereotype;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeLoader {

  private static final Map<String, Class<?>> PRIMITIVE_TYPES;
  private static final Map<Class<?>, Class<?>> PRIMITIVE_MAPPINGS;

  static {
    PRIMITIVE_TYPES = new HashMap<>();
    PRIMITIVE_TYPES.put("boolean", Boolean.TYPE);
    PRIMITIVE_TYPES.put("byte", Byte.TYPE);
    PRIMITIVE_TYPES.put("char", Character.TYPE);
    PRIMITIVE_TYPES.put("short", Short.TYPE);
    PRIMITIVE_TYPES.put("int", Integer.TYPE);
    PRIMITIVE_TYPES.put("long", Long.TYPE);
    PRIMITIVE_TYPES.put("double", Double.TYPE);
    PRIMITIVE_TYPES.put("float", Float.TYPE);
    PRIMITIVE_TYPES.put("void", Void.TYPE);

    PRIMITIVE_MAPPINGS = new HashMap<>();
    PRIMITIVE_MAPPINGS.put(Boolean.TYPE, Boolean.class);
    PRIMITIVE_MAPPINGS.put(Byte.TYPE, Byte.class);
    PRIMITIVE_MAPPINGS.put(Character.TYPE, Character.class);
    PRIMITIVE_MAPPINGS.put(Short.TYPE, Short.class);
    PRIMITIVE_MAPPINGS.put(Integer.TYPE, Integer.class);
    PRIMITIVE_MAPPINGS.put(Long.TYPE, Long.class);
    PRIMITIVE_MAPPINGS.put(Double.TYPE, Double.class);
    PRIMITIVE_MAPPINGS.put(Float.TYPE, Float.class);
    PRIMITIVE_MAPPINGS.put(Void.TYPE, Void.class);
  }

  public static Class<?> loadClass(ClassLoader classLoader, String name) throws ClassNotFoundException {
    return PRIMITIVE_TYPES.containsKey(name) ? PRIMITIVE_TYPES.get(name) : classLoader.loadClass(name);
  }

  public static Class<?> getPrimitiveClass(Class<?> primitiveType) {
    return PRIMITIVE_MAPPINGS.get(primitiveType);
  }

}
