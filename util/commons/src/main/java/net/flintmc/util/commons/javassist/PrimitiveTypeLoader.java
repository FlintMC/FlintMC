/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.commons.javassist;

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

  public static Class<?> getPrimitiveClass(String name) {
    return PRIMITIVE_TYPES.get(name);
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

      String nonNullValue =
          "((" + wrappedPrimitive.getName() + ") " + wrapped + ")." + type.getName() + "Value()";

      return String.format(
          "(%s == null ? %s : %s)",
          wrapped,
          DefaultValues.getDefaultValue(type.getName()),
          nonNullValue);
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
