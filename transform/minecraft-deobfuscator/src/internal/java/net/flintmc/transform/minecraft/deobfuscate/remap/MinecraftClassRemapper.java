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

package net.flintmc.transform.minecraft.deobfuscate.remap;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.transform.minecraft.deobfuscate.MinecraftInstructionDeobfuscator;
import net.flintmc.util.commons.io.IOUtils;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.FieldMapping;
import net.flintmc.util.mappings.MethodMapping;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and provides mappings for a {@link org.objectweb.asm.commons.ClassRemapper},
 * or in this case {@link MinecraftInstructionDeobfuscator}.
 */
@Singleton
public class MinecraftClassRemapper extends SimpleRemapper {

  private final ClassMappingProvider classMappingProvider;
  private final URLClassLoader urlClassLoader;
  private Handle lastHandle;

  @Inject
  private MinecraftClassRemapper(ClassPool pool,
                                 ClassMappingProvider classMappingProvider) {
    super(collectMappings(pool, classMappingProvider));
    this.classMappingProvider = classMappingProvider;
    this.urlClassLoader = (URLClassLoader) getClass().getClassLoader();
  }

  private static Map<String, String> collectMappings(ClassPool pool,
                                                     ClassMappingProvider classMappingProvider) {
    Map<String, String> mappings = new HashMap<>();

    for (ClassMapping classMapping : classMappingProvider
        .getDeobfuscatedClassMappings().values()) {
      String name = classMapping.getObfuscatedName().replace('.', '/');

      if (!classMapping.isDefault()) {
        if (mappings
            .put(name, classMapping.getDeobfuscatedName().replace('.', '/'))
            != null) {
          throw new IllegalStateException("Duplicate key!");
        }
      }

      addMethodAndFieldMappings(mappings, classMapping, name);

      CtClass ctClass = null;

      try {
        ctClass = pool.get(classMapping.getName());
      } catch (NotFoundException ignored) {
        // Can be ignored, because the SRG mapping
        // contains classes which do not exist in the game.
      }

      if (ctClass != null) {
        try {
          CtClass superClass = ctClass.getSuperclass();

          while (superClass != null && superClass.getPackageName() == null) {
            ClassMapping superClassMapping = classMappingProvider
                .get(superClass.getName());

            // Adds all super types methods & fields to the given class
            addMethodAndFieldMappings(mappings, superClassMapping, name);

            superClass = superClass.getSuperclass();
          }

        } catch (NotFoundException exception) {
          // If the exception is thrown, the game is broken.
          exception.printStackTrace();
        }
      }
    }

    return mappings;
  }

  private static void addMethodAndFieldMappings(
      Map<String, String> mappings, ClassMapping classMapping, String name) {
    for (MethodMapping method : classMapping.getObfuscatedMethods().values()) {
      mappings.put(name + "." + method.getObfuscatedIdentifier(),
          method.getDeobfuscatedName());
    }

    for (FieldMapping value : classMapping.getObfuscatedFields().values()) {
      if (!value.isDefault()) {
        mappings.put(name + "." + value.getObfuscatedName(),
            value.getDeobfuscatedName());
      }
    }
  }

  public List<String> getSuperClass(String clazz) {
    try {
      byte[] classData = this.retrieveClass(clazz);

      if (classData == null) {
        String mappedClass = this.map(clazz);
        if (mappedClass != null) {
          classData = this.retrieveClass(mappedClass);
        }
      }

      if (classData == null) {
        // Java internal class
        return new ArrayList<>();
      }

      ClassNode theClazz = getNode(classData);

      byte[] superClassInformation = this.retrieveClass(theClazz.superName);

      ClassNode superClass =
          superClassInformation == null
              ? null
              : getNode(superClassInformation);

      ArrayList<String> classes = new ArrayList<>();
      if (superClass != null) {
        classes.add(superClass.name);
      }
      if (theClazz.interfaces != null) {
        classes.addAll(theClazz.interfaces);
      }

      ArrayList<String> transitiveSuperClasses = new ArrayList<>();
      classes.forEach(c -> transitiveSuperClasses.addAll(getSuperClass(c)));
      classes.addAll(transitiveSuperClasses);
      return classes;
    } catch (Exception ignored) {
      // Not found, can be ignored
      return new ArrayList<>();
    }
  }

  private byte[] retrieveClass(String clazz) {
    InputStream resourceAsStream = this.urlClassLoader.getResourceAsStream(clazz.replace('.', '/') + ".class");
    if (resourceAsStream == null) return null;
    try {
      return IOUtils.readToArray(resourceAsStream);
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Parse class node.
   *
   * @param bytes Raw class data.
   * @return The class node constructed from the raw bytes.
   */
  public static ClassNode getNode(final byte[] bytes) {
    ClassReader cr = new ClassReader(bytes);
    ClassNode cn = new ClassNode();

    try {
      cr.accept(cn, ClassReader.EXPAND_FRAMES);
    } catch (Exception exception) {
      cr.accept(cn, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
    }

    return cn;
  }

  @Override
  public String map(String key) {
    return super.map(key);
  }

  @Override
  public String mapMethodName(String owner, String name, String desc) {

    String map =
        this.map(
            owner.replace('.', '/') + "." + name + desc
                .substring(0, desc.lastIndexOf(')') + 1));
    if (map == null) {

      List<String> possibleOwners = this.getSuperClass(owner.replace('.', '/'));
      if (possibleOwners != null) {
        for (String possibleOwner : possibleOwners) {
          map =
              this.map(
                  possibleOwner.replace('.', '/')
                      + "."
                      + name
                      + desc.substring(0, desc.lastIndexOf(')') + 1));

          if (map == null) {
            ClassMapping mapping = this.classMappingProvider
                .get(possibleOwner.replace('/', '.'));
            if (mapping != null) {
              String obfuscatedName = mapping.getObfuscatedName();

              map =
                  this.map(
                      obfuscatedName.replace('.', '/')
                          + "."
                          + name
                          + desc.substring(0, desc.lastIndexOf(')') + 1));

            }
          }
          if (map != null) {
            return map;
          }
        }
      }
    }

    return map != null ? map : super.mapMethodName(owner, name, desc);
  }

  @Override
  public String mapInvokeDynamicMethodName(String name, String desc) {
    if (lastHandle != null && lastHandle.getName().startsWith("lambda$")) {
      String owner = Type.getReturnType(desc).getInternalName();
      String descriptor = this.lastHandle.getDesc();
      String methodName = this.mapMethodName(owner, name, descriptor);
      lastHandle = null;
      return methodName;
    }
    lastHandle = null;
    return super.mapInvokeDynamicMethodName(name, desc);
  }

  @Override
  public Object mapValue(Object value) {
    if (value instanceof Handle) {
      lastHandle = (Handle) value;
    }
    return super.mapValue(value);
  }

  @Override
  public String mapFieldName(String owner, String name, String desc) {
    if (name.startsWith("$SwitchMap$")) {
      String switchMap = name.replace("$SwitchMap$", "");
      switchMap = switchMap.replace('$', '/');
      switchMap = mapType(switchMap);
      switchMap = switchMap.replace('/', '$');
      switchMap = "$SwitchMap$" + switchMap;
      return switchMap;
    }

    return mapMethodName(owner, name, desc);
  }

  @Override
  public String mapMethodDesc(String desc) {
    return super.mapMethodDesc(desc);
  }
}
