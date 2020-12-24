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

package net.flintmc.framework.stereotype.service;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;
import net.flintmc.launcher.LaunchController;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CtResolver {

  private static final Map<CtClass, Class<?>> classes = new HashMap<>();
  private static final Map<Integer, Method> methods = new HashMap<>();
  private static final Map<Integer, Constructor<?>> constructors = new HashMap<>();

  public static Constructor<?> get(CtConstructor ctConstructor) {
    int hash = Objects.hash(ctConstructor, ctConstructor.getDeclaringClass());
    if (!constructors.containsKey(hash)) {
      try {
        Constructor<?> declaredConstructor =
            get(ctConstructor.getDeclaringClass())
                .getDeclaredConstructor(getParameters(ctConstructor));
        declaredConstructor.setAccessible(true);
        constructors.put(hash, declaredConstructor);
      } catch (NoSuchMethodException | NotFoundException e) {
        e.printStackTrace();
      }
    }
    return constructors.get(hash);
  }

  /**
   * @param ctMethod the {@link CtMethod} to find the reflect representation from
   * @return the reflect representation of ctMethod
   */
  public static Method get(CtMethod ctMethod) {
    int hash = hash(ctMethod);
    if (!methods.containsKey(hash)) {
      try {
        Method declaredMethod =
            get(ctMethod.getDeclaringClass())
                .getDeclaredMethod(ctMethod.getName(), getParameters(ctMethod));
        declaredMethod.setAccessible(true);
        methods.put(hash, declaredMethod);
      } catch (NoSuchMethodException | NotFoundException e) {
        e.printStackTrace();
      }
    }
    return methods.get(hash);
  }

  private static Class<?>[] getParameters(CtBehavior behavior) throws NotFoundException {
    CtClass[] ctParameters = behavior.getParameterTypes();
    Class<?>[] parameters = new Class[ctParameters.length];
    for (int i = 0; i < ctParameters.length; i++) {
      parameters[i] = get(ctParameters[i]);
    }
    return parameters;
  }

  public static int hash(CtMethod ctMethod) {
    return Objects.hash(ctMethod, ctMethod.getDeclaringClass());
  }

  /**
   * @param ctClass the {@link CtClass} to find the reflect representation from
   * @return the reflect representation of ctClass
   */
  public static <T> Class<T> get(CtClass ctClass) {
    if (classes.containsKey(ctClass)) {
      return (Class<T>) classes.get(ctClass);
    }

    if (ctClass instanceof CtPrimitiveType){
      Class<T> clazz = (Class<T>) PrimitiveTypeLoader.getPrimitiveClass(ctClass.getName());
      classes.put(ctClass, clazz);
      return clazz;
    }

    try {
      CtClass baseClass = ctClass;
      int dimensions = 0;

      while (ctClass.isArray()) {
        ctClass = ctClass.getComponentType();
        ++dimensions;
      }

      Class<?> clazz = LaunchController.getInstance().getRootLoader().loadClass(ctClass.getName());
      classes.put(ctClass, clazz);

      if (dimensions != 0) {
        for (int i = 0; i < dimensions; i++) {
          clazz = Array.newInstance(clazz, 0).getClass();
        }

        classes.put(baseClass, clazz);
      }

      return (Class<T>) clazz;
    } catch (ClassNotFoundException | NotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
