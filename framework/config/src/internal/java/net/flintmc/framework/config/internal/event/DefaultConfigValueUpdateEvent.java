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

package net.flintmc.framework.config.internal.event;

import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

import javax.annotation.Nullable;

@Implement(ConfigValueUpdateEvent.class)
public class DefaultConfigValueUpdateEvent implements ConfigValueUpdateEvent {

  private final ConfigObjectReference reference;
  private final Object previousValue;
  private final Object newValue;

  @AssistedInject
  public DefaultConfigValueUpdateEvent(
      @Assisted ConfigObjectReference reference,
      @Assisted("previousValue") @Nullable Object previousValue,
      @Assisted("newValue") @Nullable Object newValue) {
    this.reference = reference;
    this.previousValue = previousValue;
    this.newValue = newValue;
  }

  /** {@inheritDoc} */
  @Override
  public ConfigObjectReference getReference() {
    return this.reference;
  }

  /** {@inheritDoc} */
  @Override
  public Object getPreviousValue() {
    return this.previousValue;
  }

  /** {@inheritDoc} */
  @Override
  public Object getNewValue() {
    return this.newValue;
  }
}
