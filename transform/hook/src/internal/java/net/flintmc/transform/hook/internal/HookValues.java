package net.flintmc.transform.hook.internal;

import java.util.HashMap;
import java.util.Map;

public class HookValues {

  private static final Map<String, String> DEFAULT_VALUES = new HashMap<>();

  static {
    registerDefault("byte", "(byte) 0");
    registerDefault("int", "(int) 0");
    registerDefault("short", "(short) 0");
    registerDefault("long", "(long) 0");

    registerDefault("double", "(double) 0.0");
    registerDefault("float", "(float) 0.0");

    registerDefault("boolean", "false");
    registerDefault("char", "' '");
    registerDefault("void", "");
  }

  private static void registerDefault(String className, String value) {
    DEFAULT_VALUES.put(className, value);
  }

  public static String getDefaultValue(String className) {
    return DEFAULT_VALUES.getOrDefault(className, "null");
  }
}
