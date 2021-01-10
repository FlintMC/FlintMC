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
import javassist.CtField;
import javassist.NotFoundException;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Implements an {@link Identifier} to locate {@link DetectableAnnotation}s located at field level.
 *
 * @see Identifier
 */
public class FieldIdentifier implements Identifier<CtField> {

  private final String owner;
  private final String name;

  public FieldIdentifier(String owner, String name) {
    this.owner = owner;
    this.name = name;
  }

  /**
   * @return The class name of the declaring class of the field represented by this identifier
   */
  public String getOwner() {
    return this.owner;
  }

  /**
   * @return The field name of this identifier
   */
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtField getLocation() {
    try {
      return ClassPool.getDefault()
          .get(this.getOwner())
          .getDeclaredField(this.getName());
    } catch (NotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
