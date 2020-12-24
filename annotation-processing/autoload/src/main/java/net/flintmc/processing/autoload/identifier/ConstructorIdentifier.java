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

package net.flintmc.processing.autoload.identifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

import java.util.function.BiFunction;

public class ConstructorIdentifier implements Identifier<CtConstructor> {

  private final String owner;
  private final String[] parameters;
  private BiFunction<ConstructorIdentifier, String, String> ownerConverter = (constructorIdentifier, owner) -> owner;
  private BiFunction<ConstructorIdentifier, String[], String[]> parametersConverter = (constructorIdentifier, parameters) -> parameters;

  public ConstructorIdentifier(String owner, String... parameters) {
    this.owner = owner;
    this.parameters = parameters;
  }

  public String getOwner() {
    return this.ownerConverter.apply(this, this.owner);
  }

  public String[] getParameters() {
    return this.parametersConverter.apply(this, this.parameters);
  }

  public ConstructorIdentifier setParametersConverter(BiFunction<ConstructorIdentifier, String[], String[]> parametersConverter) {
    this.parametersConverter = parametersConverter;
    return this;
  }

  public ConstructorIdentifier setOwnerConverter(BiFunction<ConstructorIdentifier, String, String> ownerConverter) {
    this.ownerConverter = ownerConverter;
    return this;
  }

  @Override
  public CtConstructor getLocation() {
    try {
      return ClassPool.getDefault().get(this.getOwner()).getDeclaredConstructor(ClassPool.getDefault().get(this.getParameters()));
    } catch (NotFoundException exception) {
      throw new IllegalStateException(exception);
    }
  }
}
