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

package net.flintmc.framework.inject.internal;

import com.google.common.base.Preconditions;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Implement(InjectedFieldBuilder.class)
public class DefaultInjectedFieldBuilder implements InjectedFieldBuilder {

  private final InjectionUtils injectionUtils;

  private CtClass target;
  private String injectedTypeName;
  private String fieldName;
  private boolean singletonInstance;
  private boolean notStatic;

  @AssistedInject
  private DefaultInjectedFieldBuilder(InjectionUtils injectionUtils) {
    this.injectionUtils = injectionUtils;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder target(CtClass target) {
    this.target = target;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(Class<?> type) {
    return this.inject(type.getName());
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(CtClass type) {
    return this.inject(type.getName());
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(String typeName) {
    this.injectedTypeName = typeName;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder fieldName(String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder multipleInstances() {
    this.singletonInstance = false;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder notStatic() {
    this.notStatic = true;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public CtField generate() throws CannotCompileException {
    Preconditions.checkNotNull(this.target, "No target defined");
    Preconditions.checkNotNull(this.injectedTypeName, "No inject type defined");

    String fieldName =
        this.fieldName != null ? this.fieldName : this.injectionUtils.generateInjectedFieldName();

    return this.injectionUtils.addInjectedField(
        this.target, fieldName, this.injectedTypeName, this.singletonInstance, !this.notStatic);
  }
}
