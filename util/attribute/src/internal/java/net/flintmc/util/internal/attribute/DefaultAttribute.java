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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.util.attribute.Attribute;

public class DefaultAttribute implements Attribute {

  private final String key;
  private final float defaultValue;
  private final float maximumValue;
  private final boolean shared;

  @AssistedInject
  public DefaultAttribute(
      @Assisted("key") String key,
      @Assisted("defaultValue") float defaultValue,
      @Assisted("maximumVale") float maximumValue) {
    this(key, defaultValue, maximumValue, true);
  }

  @AssistedInject
  public DefaultAttribute(
      @Assisted("key") String key,
      @Assisted("defaultValue") float defaultValue,
      @Assisted("maximumValue") float maximumValue,
      @Assisted("shared") boolean shared) {
    if (defaultValue > maximumValue) {
      throw new IllegalArgumentException(
          String.format(
              "Default value cannot be greater than the maximum allowed. (Default: %s, Maximum: %s)",
              defaultValue, maximumValue));
    }
    this.key = key;
    this.defaultValue = defaultValue;
    this.maximumValue = maximumValue;
    this.shared = shared;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey() {
    return this.key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getMaximumValue() {
    return this.maximumValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShared() {
    return this.shared;
  }
}
