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

package net.flintmc.util.mappings.utils;

import java.util.Map;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;

/**
 * <code>MappingUtils</code> provides several utilities to parse mappings. It focuses on general
 * purposes things like generating a descriptor but also contains a few extracted methods to tidy up
 * the mapping parser.
 */
public final class MappingUtils {

  private static ClassMappingProvider classMappingProvider;

  private MappingUtils() {
  }

  /**
   * Deobfuscate a method descriptor. This method is a post-processing step, meaning that the <code>
   * classMappings</code> parameter is supposed to be complete. Otherwise, the method descriptor
   * cannot be deobfuscated properly, resulting in a partially/not deobfuscated method descriptor.
   *
   * @param classMappings        Class mappings.
   * @param obfuscatedDescriptor Obfuscated method descriptor.
   * @return A deobfuscated method descriptor.
   */
  public static String deobfuscateMethodDescriptor(
      final Map<String, ClassMapping> classMappings, final String obfuscatedDescriptor) {
    StringBuilder descriptor = new StringBuilder(), className = new StringBuilder();
    boolean readClassName = false;

    for (char c : obfuscatedDescriptor.toCharArray()) {
      if (readClassName) {
        if (c == ';') {
          descriptor.append("L");

          if (classMappings.containsKey(className.toString())) {
            descriptor.append(
                classMappings.get(className.toString()).getDeobfuscatedName().replace(".", "/"));
          } else {
            descriptor.append(className);
          }

          descriptor.append(";");
          className.setLength(0);
          readClassName = false;
        } else {
          className.append(c);
        }
      } else if (c == 'L') {
        readClassName = true;
      } else {
        descriptor.append(c);
      }
    }

    return descriptor.toString();
  }

  /**
   * Generate a descriptor for specified classes.
   *
   * @param classes An array of classes.
   * @return A descriptor based on specified classes.
   */
  public static String generateDescriptor(final Class<?>... classes) {
    StringBuilder descriptor = new StringBuilder();

    for (Class<?> clazz : classes) {
      descriptor.append(generateDescriptor(clazz));
    }

    return descriptor.toString();
  }


  /**
   * Generate a descriptor for one class.
   *
   * @param clazz A class.
   * @return A descriptor based on the specified class.
   */
  public static String generateDescriptor(final Class<?> clazz) {
    if (clazz.isPrimitive()) {
      if (clazz == byte.class) {
        return "B";
      }
      if (clazz == char.class) {
        return "C";
      }
      if (clazz == double.class) {
        return "D";
      }
      if (clazz == float.class) {
        return "F";
      }
      if (clazz == int.class) {
        return "I";
      }
      if (clazz == long.class) {
        return "J";
      }
      if (clazz == short.class) {
        return "S";
      }
      if (clazz == boolean.class) {
        return "Z";
      }
      if (clazz == void.class) {
        return "V";
      }
    } else if (clazz.isArray()) {
      return "[" + generateArrayDescriptor(clazz.getComponentType());
    }

    return "L" + clazz.getName().replace(".", "/") + ";";
  }

  /**
   * Generate an array descriptor for one class.
   *
   * @param clazz A class.
   * @return An array descriptor based on the specified class.
   */
  private static String generateArrayDescriptor(final Class<?> clazz) {
    if (clazz.isArray()) {
      return "[" + generateArrayDescriptor(clazz.getComponentType());
    }
    return generateDescriptor(clazz);
  }

  /**
   * Generate a descriptor for specified classes.
   *
   * @param classes An array of classes.
   * @return A descriptor based on specified classes.
   */
  public static String generateDescriptor(final CtClass... classes) {
    StringBuilder descriptor = new StringBuilder();

    for (CtClass clazz : classes) {
      descriptor.append(generateDescriptor(clazz));
    }

    return descriptor.toString();
  }

  /**
   * Generate a descriptor for specified classes.
   *
   * @param classes An array of classes.
   * @return A descriptor based on specified classes.
   */
  public static String generateDescriptor(boolean obfuscated, final CtClass... classes) {
    StringBuilder descriptor = new StringBuilder();

    for (CtClass clazz : classes) {
      descriptor.append(generateDescriptor(obfuscated, clazz));
    }

    return descriptor.toString();
  }

  /**
   * Generate a descriptor for one class.
   *
   * @param clazz A class.
   * @return A descriptor based on the specified class.
   */
  public static String generateDescriptor(final CtClass clazz) {
    if (clazz.isPrimitive()) {
      if (clazz == CtClass.byteType) {
        return "B";
      }
      if (clazz == CtClass.charType) {
        return "C";
      }
      if (clazz == CtClass.doubleType) {
        return "D";
      }
      if (clazz == CtClass.floatType) {
        return "F";
      }
      if (clazz == CtClass.intType) {
        return "I";
      }
      if (clazz == CtClass.longType) {
        return "J";
      }
      if (clazz == CtClass.shortType) {
        return "S";
      }
      if (clazz == CtClass.booleanType) {
        return "Z";
      }
      if (clazz == CtClass.voidType) {
        return "V";
      }
    } else if (clazz.isArray()) {
      try {
        return "[" + generateArrayDescriptor(clazz.getComponentType());
      } catch (NotFoundException ignored) {
        throw new RuntimeException("Unreachable condition hit: Array is not an array.");
      }
    }

    return "L" + clazz.getName().replace(".", "/") + ";";
  }


  /**
   * Generate a descriptor for one class.
   *
   * @param clazz A class.
   * @return A descriptor based on the specified class.
   */
  public static String generateDescriptor(boolean obfuscated, final CtClass clazz) {
    if (clazz.isPrimitive()) {
      if (clazz == CtClass.byteType) {
        return "B";
      }
      if (clazz == CtClass.charType) {
        return "C";
      }
      if (clazz == CtClass.doubleType) {
        return "D";
      }
      if (clazz == CtClass.floatType) {
        return "F";
      }
      if (clazz == CtClass.intType) {
        return "I";
      }
      if (clazz == CtClass.longType) {
        return "J";
      }
      if (clazz == CtClass.shortType) {
        return "S";
      }
      if (clazz == CtClass.booleanType) {
        return "Z";
      }
      if (clazz == CtClass.voidType) {
        return "V";
      }
    } else if (clazz.isArray()) {
      try {
        return "[" + generateArrayDescriptor(clazz.getComponentType());
      } catch (NotFoundException ignored) {
        throw new RuntimeException("Unreachable condition hit: Array is not an array.");
      }
    }
    ClassMapping classMapping = classMappingProvider.get(clazz.getName());
    String name = classMapping != null ? (obfuscated ? classMapping.getObfuscatedName()
        : classMapping.getDeobfuscatedName()) : clazz.getName();
    return "L" + name.replace(".", "/") + ";";
  }

  /**
   * Generate an array descriptor for one class.
   *
   * @param clazz A class.
   * @return An array descriptor based on the specified class.
   */
  private static String generateArrayDescriptor(final CtClass clazz) {
    if (clazz.isArray()) {
      try {
        return "[" + generateArrayDescriptor(clazz.getComponentType());
      } catch (NotFoundException ignored) {
        throw new RuntimeException("Unreachable condition hit: Array is not an array.");
      }
    }
    return generateDescriptor(clazz);
  }

  public static ClassMappingProvider getClassMappingProvider() {
    if (classMappingProvider == null) {
      classMappingProvider = InjectionHolder.getInjectedInstance(ClassMappingProvider.class);
    }
    return classMappingProvider;
  }
}
