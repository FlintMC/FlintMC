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
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s located at class level.
 *
 * @see Identifier
 */
public class ClassIdentifier implements Identifier<CtClass> {

  private final String name;

  public ClassIdentifier(String name) {
    this.name = name;
  }

  /**
   * @return The class name of this identifier
   */
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass getLocation() {
    try {
      return ClassPool.getDefault().get(this.getName());
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
