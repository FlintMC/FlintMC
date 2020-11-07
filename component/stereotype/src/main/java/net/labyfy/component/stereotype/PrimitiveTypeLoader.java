package net.labyfy.component.stereotype;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeLoader {

  private static final Map<String, Class<?>> PRIMITIVE_TYPES;
  private static final BiMap<Class<?>, Class<?>> PRIMITIVE_MAPPINGS;

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

    PRIMITIVE_MAPPINGS = HashBiMap.create();
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

  public static Class<?> getPrimitiveClass(Class<?> wrappedType) {
    return PRIMITIVE_MAPPINGS.inverse().get(wrappedType);
  }

  public static Class<?> getWrappedClass(Class<?> primitiveType) {
    return PRIMITIVE_MAPPINGS.get(primitiveType);
  }

  public static Class<?> getWrappedClass(String name) {
    Class<?> type = PRIMITIVE_TYPES.get(name);
    return type != null ? PRIMITIVE_MAPPINGS.get(type) : null;
  }

  public static String asPrimitiveSource(CtClass type, String wrapped) {
    Class<?> wrappedPrimitive = PrimitiveTypeLoader.getWrappedClass(type.getName());
    if (wrappedPrimitive != null) {
      if (Number.class.isAssignableFrom(wrappedPrimitive)) {
        wrappedPrimitive = Number.class;
      }

      return "((" + wrappedPrimitive.getName() + ") " + wrapped + ")." + type.getName() + "Value()";
    }

    return wrapped;
  }

  public static String asWrappedPrimitiveSource(CtClass type, String primitive) {
    if (type.isPrimitive()) {
      return ((CtPrimitiveType) type).getWrapperName() + ".valueOf((" + type.getName() + ") " + primitive + ")";
    }
    return primitive;
  }

}
