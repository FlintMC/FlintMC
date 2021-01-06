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

package net.flintmc.mcapi.items.inventory;

/**
 * An enumeration of all available slot types.
 */
public enum EquipmentSlotType {
  MAIN_HAND(Group.HAND, 0),
  OFF_HAND(Group.HAND, 1),
  FEET(Group.ARMOR, 0),
  LEGS(Group.ARMOR, 1),
  CHEST(Group.ARMOR, 2),
  HEAD(Group.ARMOR, 3);

  private final EquipmentSlotType.Group group;
  private final int index;

  EquipmentSlotType(Group group, int index) {
    this.group = group;
    this.index = index;
  }

  /**
   * Retrieves the group of the slot type.
   *
   * @return The slot type group.
   */
  public Group getGroup() {
    return group;
  }

  /**
   * Retrieves the index of the slot type.
   *
   * @return The slot type index.
   */
  public int getIndex() {
    return index;
  }

  /**
   * An enumeration of all availables groups.
   */
  enum Group {
    ARMOR,
    HAND
  }
}
