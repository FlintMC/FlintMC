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

package net.flintmc.util.internal.attribute;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.attribute.AttributeModifier;
import net.flintmc.util.attribute.AttributeOperation;

@Implement(AttributeModifier.class)
public class DefaultAttributeModifier implements AttributeModifier {

  private final UUID uniqueId;
  private final String name;
  private final float amount;
  private final AttributeOperation operation;

  @AssistedInject
  public DefaultAttributeModifier(
      @Assisted("name") String name,
      @Assisted("amount") float amount,
      @Assisted("operation") AttributeOperation operation) {
    this(UUID.randomUUID(), name, amount, operation);
  }

  @AssistedInject
  public DefaultAttributeModifier(
      @Assisted("uniqueId") UUID uniqueId,
      @Assisted("name") String name,
      @Assisted("amount") float amount,
      @Assisted("operation") AttributeOperation operation) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.amount = amount;
    this.operation = operation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAmount() {
    return this.amount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AttributeOperation getOperation() {
    return this.operation;
  }
}
