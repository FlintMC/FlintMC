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

package net.flintmc.util.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration that representing all attribute operations.
 */
public enum AttributeOperation {
  ADDITION(0),
  MULTIPLY_BASE(1),
  MULTIPLY_TOTAL(2);

  private static final Map<Integer, AttributeOperation> BY_IDENTIFIER = new HashMap<>();

  static {
    for (AttributeOperation value : values()) {
      BY_IDENTIFIER.put(value.identifier, value);
    }
  }

  private final int identifier;

  AttributeOperation(int identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves an attribute operation with the given {@code identifier}.
   *
   * @param identifier The identifier of the attribute operation.
   * @return An attribute operation or {@code null}.
   */
  public static AttributeOperation fromIdentifier(int identifier) {
    return BY_IDENTIFIER.get(identifier);
  }

  /**
   * Retrieves the identifier of this operation.
   *
   * @return The operation's identifier.
   */
  public int getIdentifier() {
    return identifier;
  }
}
