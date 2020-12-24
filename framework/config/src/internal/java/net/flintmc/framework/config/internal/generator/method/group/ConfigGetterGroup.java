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

package net.flintmc.framework.config.internal.generator.method.group;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.SignatureAttribute;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.internal.generator.method.GenericMethodHelper;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigGetterSetter;
import net.flintmc.framework.config.internal.generator.method.defaults.ConfigMultiGetterSetter;
import net.flintmc.framework.config.serialization.ConfigSerializationService;

import java.util.Map;

@Singleton
public class ConfigGetterGroup implements ConfigMethodGroup {

  private final ClassPool pool;
  private final GenericMethodHelper methodHelper;
  private final ConfigSerializationService serializationService;

  @Inject
  private ConfigGetterGroup(
      ClassPool pool,
      GenericMethodHelper methodHelper,
      ConfigSerializationService serializationService) {
    this.pool = pool;
    this.methodHelper = methodHelper;
    this.serializationService = serializationService;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getPossiblePrefixes() {
    return new String[] {"get", "is"};
  }

  /** {@inheritDoc} */
  @Override
  public ConfigMethod resolveMethod(
      GeneratingConfig config, CtClass type, String entryName, CtMethod method)
      throws NotFoundException {
    CtClass methodType = method.getReturnType();
    if (methodType.equals(CtClass.voidType)) {
      return null;
    }

    CtClass[] parameters = method.getParameterTypes();
    if (parameters.length == 0) {
      if (methodType.getName().equals(Map.class.getName())
          && entryName.startsWith(ConfigMultiGetterSetter.ALL_PREFIX)) {
        return this.methodHelper.generateMultiGetterSetter(
            config,
            type,
            method,
            signature -> (SignatureAttribute.ClassType) signature.getReturnType(),
            entryName.substring(ConfigMultiGetterSetter.ALL_PREFIX.length()));
      }

      return new ConfigGetterSetter(this.serializationService, config, type, entryName, methodType);
    }

    if (parameters.length == 1) {
      if (parameters[0].equals(CtClass.voidType)) {
        return null;
      }

      return new ConfigMultiGetterSetter(
          this.serializationService,
          config,
          type,
          entryName,
          this.pool.get(Map.class.getName()),
          parameters[0],
          methodType);
    }

    throw new IllegalArgumentException("Getter can only have either no or one parameter");
  }
}
