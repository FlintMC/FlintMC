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

package net.flintmc.framework.config.internal.generator;

import javassist.CtClass;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

import java.util.*;

@Implement(GeneratingConfig.class)
public class DefaultGeneratingConfig implements GeneratingConfig {

  private final ClassLoader classLoader;
  private final String name;
  private final CtClass baseClass;
  private final Collection<ConfigMethod> allMethods;
  private final Collection<CtClass> implementedInterfaces;
  private final Map<String, CtClass> generatedImplementations;

  @AssistedInject
  private DefaultGeneratingConfig(
      ImplementationGenerator generator, @Assisted("baseClass") CtClass baseClass) {
    this.classLoader = generator.getClassLoader();
    this.name = baseClass.getName();
    this.baseClass = baseClass;
    this.allMethods = new HashSet<>();
    this.implementedInterfaces = new HashSet<>();
    this.generatedImplementations = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getBaseClass() {
    return this.baseClass;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<ConfigMethod> getAllMethods() {
    return this.allMethods;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<CtClass> getImplementedInterfaces() {
    return Collections.unmodifiableCollection(this.implementedInterfaces);
  }

  /** {@inheritDoc} */
  @Override
  public Collection<CtClass> getGeneratedImplementations() {
    return Collections.unmodifiableCollection(this.generatedImplementations.values());
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getGeneratedImplementation(String baseName) {
    return this.generatedImplementations.get(baseName);
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getGeneratedImplementation(String baseName, CtClass def) {
    return this.generatedImplementations.getOrDefault(baseName, def);
  }

  /** {@inheritDoc} */
  @Override
  public void bindGeneratedImplementation(CtClass base, CtClass implementation) {
    this.generatedImplementations.put(base.getName(), implementation);
    this.implementedInterfaces.add(base);
  }

  /** {@inheritDoc} */
  @Override
  public ClassLoader getClassLoader() {
    return this.classLoader;
  }
}
