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

package net.flintmc.transform.javassist;

import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public enum CtClassFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String className) throws NotFoundException {
      Collection<CtClass> classes = CtClassFilters.collectSuperClassesRecursive(source);

      for (CtClass ctClass : classes) {
        if (className.equals(ctClass.getName())) {
          return true;
        }
      }

      return false;
    }
  };

  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass)
      throws NotFoundException {
    Collection<CtClass> classes = new HashSet<>();

    if (ctClass.getSuperclass() != null) {
      classes.add(ctClass.getSuperclass());
      classes.addAll(collectSuperClassesRecursive(ctClass.getSuperclass()));
    }

    classes.addAll(Arrays.asList(ctClass.getInterfaces()));

    for (CtClass value : ctClass.getInterfaces()) {
      classes.addAll(CtClassFilters.collectSuperClassesRecursive(value));
    }

    return Collections.unmodifiableCollection(classes);
  }

  public abstract boolean test(CtClass source, String className) throws NotFoundException;
}
