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

package net.flintmc.framework.config.event;

import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import javax.annotation.Nullable;

/**
 * This event will be fired whenever the value in a {@link ConfigObjectReference} is being updated via {@link
 * ConfigObjectReference#setValue(Object)}. It will not be fired if the value is updated via a setter in
 * the config itself.
 * <p>
 * This event will be fired in both the {@link Subscribe.Phase#PRE} and {@link Subscribe.Phase#POST} phases.
 *
 * @see Subscribe
 */
@Subscribable({Phase.PRE, Phase.POST})
public interface ConfigValueUpdateEvent extends Event {

  /**
   * Retrieves the reference where the value has been updated.
   *
   * @return The non-null reference where the value has been updated
   */
  ConfigObjectReference getReference();

  /**
   * Retrieves the value that was set in the config before it has been updated.
   *
   * @return The nullable value from before the update
   */
  Object getPreviousValue();

  /**
   * Retrieves the value that is now set in the config.
   *
   * @return The nullable value from after the update
   */
  Object getNewValue();

  /**
   * Factory for the {@link ConfigValueUpdateEvent}.
   */
  @AssistedFactory(ConfigValueUpdateEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigValueUpdateEvent} with the given values.
     *
     * @param reference     The non-null reference where the value has been updated
     * @param previousValue The nullable value from before the update
     * @param newValue      The nullable value from after the update
     * @return The new non-null {@link ConfigValueUpdateEvent}
     */
    ConfigValueUpdateEvent create(@Assisted ConfigObjectReference reference,
                                  @Assisted("previousValue") @Nullable Object previousValue,
                                  @Assisted("newValue") @Nullable Object newValue);

  }

}
