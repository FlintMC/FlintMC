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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.attribute.Attribute;
import net.flintmc.util.attribute.AttributeInstance;
import net.flintmc.util.attribute.AttributeModifier;
import net.flintmc.util.attribute.AttributeOperation;

@Implement(AttributeInstance.class)
public class DefaultAttributeInstance implements AttributeInstance {

  private final Attribute attribute;
  private final Map<UUID, AttributeModifier> modifiers;
  private final Consumer<AttributeInstance> propertyChangeListener;
  private float baseValue;
  private float cachedValue;
  private boolean dirty;

  @AssistedInject
  public DefaultAttributeInstance(
      @Assisted("attribute") Attribute attribute,
      @Assisted("propertyChangeListener") Consumer<AttributeInstance> propertyChangeListener) {
    this.attribute = attribute;
    this.modifiers = new HashMap<>();
    this.propertyChangeListener = propertyChangeListener;
    this.baseValue = attribute.getDefaultValue();
    this.cachedValue = 0.0F;
    this.dirty = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute getAttribute() {
    return this.attribute;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBaseValue() {
    return this.baseValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBaseValue(float value) {
    this.baseValue = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addModifier(AttributeModifier modifier) {
    if (this.modifiers.putIfAbsent(modifier.getUniqueId(), modifier) == null) {
      this.executeDirty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeModifier(AttributeModifier modifier) {
    if (this.modifiers.remove(modifier.getUniqueId()) != null) {
      this.executeDirty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<AttributeModifier> getModifiers() {
    return this.modifiers.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getValue() {
    if (this.dirty) {
      this.cachedValue = this.processModifiers();
      this.dirty = false;
    }

    return this.cachedValue;
  }

  /**
   * Recalculate the value of this attribute instance using the modifiers.
   *
   * @return The attribute value.
   */
  private float processModifiers() {
    float base = this.getBaseValue();

    for (AttributeModifier attributeModifier :
        this.getAttributeModifiers(AttributeOperation.ADDITION)) {
      base += attributeModifier.getAmount();
    }

    float result = base;

    for (AttributeModifier attributeModifier :
        this.getAttributeModifiers(AttributeOperation.MULTIPLY_BASE)) {
      result += (base * attributeModifier.getAmount());
    }

    for (AttributeModifier attributeModifier :
        this.getAttributeModifiers(AttributeOperation.MULTIPLY_TOTAL)) {
      result += (1.0F + attributeModifier.getAmount());
    }

    return Math.min(result, this.getAttribute().getMaximumValue());
  }

  private AttributeModifier[] getAttributeModifiers(AttributeOperation operation) {
    return this.modifiers.values().stream()
        .filter(modifier -> modifier.getOperation() == operation)
        .toArray(AttributeModifier[]::new);
  }

  private void executeDirty() {
    if (!this.dirty) {
      this.dirty = true;

      if (this.propertyChangeListener != null) {
        this.propertyChangeListener.accept(this);
      }
    }
  }
}
