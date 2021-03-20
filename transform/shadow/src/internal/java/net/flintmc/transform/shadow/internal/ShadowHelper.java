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

package net.flintmc.transform.shadow.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.launcher.LaunchController;
import net.flintmc.metaprogramming.identifier.MethodIdentifier;
import net.flintmc.util.mappings.utils.RemappingMethodLocationResolver;
import java.util.Arrays;

@Singleton
public class ShadowHelper {

  private final RemappingMethodLocationResolver remappingMethodLocationResolver;

  @Inject
  private ShadowHelper() {
    this.remappingMethodLocationResolver = new RemappingMethodLocationResolver();
  }

  public boolean hasMethod(CtClass ctClass, String name, CtClass[] parameters)
      throws NotFoundException {
    return this.getMethod(ctClass, name, parameters) != null;
  }

  public CtMethod getMethod(CtClass ctClass, String name, CtClass[] parameters)
      throws NotFoundException {
    for (CtMethod method : ctClass.getDeclaredMethods()) {
      if (method.getName().equals(name) && Arrays
          .equals(method.getParameterTypes(), parameters)) {
        return method;
      }
    }

    return null;
  }

  public CtMethod getLocation(MethodIdentifier identifier)
      throws ClassNotFoundException {
    // we need to load the interface class so parameter and return types get appropriately
    // remapped before getLocation is called
    Class.forName(identifier.getOwner(), false,
        LaunchController.getInstance().getRootLoader());
    identifier.setMethodResolver(() -> this.remappingMethodLocationResolver
        .getLocation(identifier.getOwner(), identifier.getName(),
            identifier.getParameters()));
    return identifier.getLocation();
  }

}
