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

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;

public class RemappingMethodLocationResolver {

  private final ClassMappingProvider classMappingProvider;

  public RemappingMethodLocationResolver() {
    this.classMappingProvider = InjectionHolder
        .getInjectedInstance(ClassMappingProvider.class);
  }

  public CtMethod getLocation(String owner, String name, String[] parameters) {
    try {

      String[] mappedParamNames = new String[parameters.length];

      for (int i = 0; i < mappedParamNames.length; i++) {
        ClassMapping mapping = classMappingProvider.get(parameters[i]);
        if (mapping != null) {
          mappedParamNames[i] = mapping.getName();
        } else {
          mappedParamNames[i] = parameters[i];
        }
      }
      try {
        return ClassPool.getDefault().get(owner)
            .getDeclaredMethod(name,
                ClassPool.getDefault().get(mappedParamNames));
      } catch (NotFoundException e) {
        e.printStackTrace();
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

}
