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

package net.flintmc.framework.config.internal.generator.method;

import com.google.inject.Inject;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.ConfigExclude;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.generator.method.ConfigMethodResolver;
import net.flintmc.framework.config.internal.generator.method.group.ConfigGetterGroup;
import net.flintmc.framework.config.internal.generator.method.group.ConfigMethodGroup;
import net.flintmc.framework.config.internal.generator.method.group.ConfigSetterGroup;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.inject.implement.Implement;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Implement(ConfigMethodResolver.class)
public class DefaultConfigMethodResolver implements ConfigMethodResolver {

  private final ConfigSerializationService serializationService;
  private final Collection<ConfigMethodGroup> groups;

  @Inject
  public DefaultConfigMethodResolver(
      ConfigSerializationService serializationService,
      ConfigGetterGroup getterGroup,
      ConfigSetterGroup setterGroup) {
    this.serializationService = serializationService;
    this.groups = Arrays.asList(getterGroup, setterGroup);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveMethods(GeneratingConfig config) throws NotFoundException {
    this.resolveMethods(config, config.getBaseClass(), new String[0]);
  }

  private void resolveMethods(GeneratingConfig config, CtClass type, String[] prefix)
      throws NotFoundException {
    this.resolveMethods(config, type, type, prefix, new HashSet<>());
  }

  private void resolveMethods(
      GeneratingConfig config,
      CtClass baseType,
      CtClass type,
      String[] prefix,
      Collection<String> handledMethods)
      throws NotFoundException {

    // We cannot use type.getMethods(), this doesn't keep the order of the methods
    // inside of the class

    for (CtMethod method : type.getDeclaredMethods()) {
      if (handledMethods.contains(method.getName())) {
        continue;
      }
      handledMethods.add(method.getName());
      this.handleMethod(config, baseType, method, prefix);
    }

    CtClass superClass = type.getSuperclass();
    if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
      this.resolveMethods(config, baseType, superClass, prefix, handledMethods);
    }

    for (CtClass ifc : type.getInterfaces()) {
      this.resolveMethods(config, baseType, ifc, prefix, handledMethods);
    }
  }

  private void handleMethod(GeneratingConfig config, CtClass type, CtMethod method, String[] prefix)
      throws NotFoundException {
    if (!method.isEmpty()) { // default implementation in the interface
      return;
    }

    String declaring = method.getDeclaringClass().getName();
    if (declaring.equals(Object.class.getName())
        || declaring.equals(ParsedConfig.class.getName())) {
      return;
    }
    if (method.hasAnnotation(ConfigExclude.class)) {
      return;
    }

    String name = method.getName();

    this.tryGroups(config, name, type, prefix, method);
  }

  private void tryGroups(
      GeneratingConfig config, String name, CtClass type, String[] prefix, CtMethod method)
      throws NotFoundException {
    for (ConfigMethodGroup group : this.groups) {
      for (String groupPrefix : group.getPossiblePrefixes()) {
        if (!name.startsWith(groupPrefix)) {
          continue;
        }

        String entryName = name.substring(groupPrefix.length());
        if (this.handleGroup(config, group, type, prefix, entryName, method)) {
          return;
        }

        break;
      }
    }
  }

  private boolean handleGroup(
      GeneratingConfig config,
      ConfigMethodGroup group,
      CtClass type,
      String[] prefix,
      String entryName,
      CtMethod method)
      throws NotFoundException {
    Collection<ConfigMethod> methods = config.getAllMethods();
    ConfigMethod configMethod = group.resolveMethod(config, type, entryName, method);

    if (configMethod == null) {
      return false;
    }
    ConfigMethodInfo info = configMethod.getInfo();

    if (!this.containsMethod(prefix, info.getConfigName(), methods)) {
      info.setPathPrefix(prefix);

      methods.add(configMethod);

      for (CtClass subType : configMethod.getTypes()) {
        if (subType.isInterface()
            && !this.serializationService.hasSerializer(subType)
            && method.getParameterTypes().length == 0) {
          String[] newPrefix = Arrays.copyOf(prefix, prefix.length + 1);
          newPrefix[newPrefix.length - 1] = info.getConfigName();

          this.resolveMethods(config, subType, newPrefix);
        }
      }

      return true;
    }

    return false;
  }

  private boolean containsMethod(String[] prefix, String name, Collection<ConfigMethod> methods) {
    for (ConfigMethod method : methods) {
      ConfigMethodInfo info = method.getInfo();
      if (Arrays.equals(prefix, info.getPathPrefix()) && info.getConfigName().equals(name)) {
        return true;
      }
    }
    return false;
  }
}
