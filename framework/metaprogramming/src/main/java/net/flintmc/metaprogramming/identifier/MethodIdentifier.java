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

package net.flintmc.metaprogramming.identifier;

import javassist.CtMethod;
import net.flintmc.metaprogramming.DetectableAnnotation;

import java.util.function.Supplier;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s
 * located at method level.
 *
 * @see Identifier
 */
public class MethodIdentifier implements Identifier<CtMethod> {

  private final String owner;
  private final String name;
  private final String[] parameters;
  private Supplier<CtMethod> methodResolver = () -> {
    throw new RuntimeException(new IllegalStateException(
        "Couldn't retrieve method location because a method resolve has not "
            + "been set."));
  };

  public MethodIdentifier(String owner, String name, String... parameters) {
    this.owner = owner;
    this.name = name;
    this.parameters = parameters;
  }

  public void setMethodResolver(Supplier<CtMethod> methodResolver) {
    this.methodResolver = methodResolver;
  }

  /**
   * @return The class name of the declaring class of the method represented by
   * this identifier
   */
  public String getOwner() {
    return this.owner;
  }

  /**
   * @return The parameter type names of the method represented by this
   * identifier
   */
  public String[] getParameters() {
    return this.parameters;
  }

  /**
   * @return The method name of this identifier
   */
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtMethod getLocation() {
    return this.methodResolver.get();
  }
}
