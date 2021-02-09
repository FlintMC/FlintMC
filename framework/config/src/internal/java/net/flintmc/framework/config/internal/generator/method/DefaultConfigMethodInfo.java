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

import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import java.util.HashMap;
import java.util.Map;

@Implement(ConfigMethodInfo.class)
public class DefaultConfigMethodInfo implements ConfigMethodInfo {

  private final GeneratingConfig config;
  private final CtClass declaringClass;
  private final String configName;
  private final CtClass methodType;

  private final Map<String, String> fieldValues;

  private String[] pathPrefix;
  private boolean implementedMethods;
  private boolean addedInterfaceMethods;

  @AssistedInject
  private DefaultConfigMethodInfo(
      @Assisted("config") GeneratingConfig config,
      @Assisted("declaringClass") CtClass declaringClass,
      @Assisted("configName") String configName,
      @Assisted("methodType") CtClass methodType) {
    this.config = config;
    this.declaringClass = declaringClass;
    this.configName = configName;
    this.methodType = methodType;

    this.fieldValues = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GeneratingConfig getConfig() {
    return this.config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass getDeclaringClass() {
    return this.declaringClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getConfigName() {
    return this.configName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass getStoredType() {
    return this.methodType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getPathPrefix() {
    return this.pathPrefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathPrefix(String[] pathPrefix) throws IllegalStateException {
    if (this.pathPrefix != null) {
      throw new IllegalStateException("The pathPrefix cannot be set twice");
    }
    this.pathPrefix = pathPrefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFieldValuesCreator() {
    if (this.fieldValues.isEmpty()) {
      return "";
    }

    StringBuilder builder = new StringBuilder();

    this.fieldValues.forEach((name, value) ->
        builder.append("this.").append(name).append('=').append(value).append(';'));

    return builder.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeField(String fieldName, String fieldValue) {
    this.fieldValues.put(fieldName, fieldValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireNoImplementation() {
    this.implementedMethods = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasImplementedExistingMethods() {
    return this.implementedMethods;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void implementedExistingMethods() {
    this.implementedMethods = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addedInterfaceMethods() {
    this.addedInterfaceMethods = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAddedInterfaceMethods() {
    return this.addedInterfaceMethods;
  }

}
