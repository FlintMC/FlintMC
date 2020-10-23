package net.labyfy.internal.component.config.generator;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveClassLoader {

  private static final Map<String, Class<?>> PRIMITIVE_TYPES;

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
  }

  public static Class<?> loadClass(ClassLoader classLoader, String name) throws ClassNotFoundException {
    return PRIMITIVE_TYPES.containsKey(name) ? PRIMITIVE_TYPES.get(name) : classLoader.loadClass(name);
  }

}
