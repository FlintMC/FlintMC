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

package net.flintmc.mcapi.internal.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.FieldOfViewModifier;
import net.flintmc.mcapi.player.event.FieldOfViewEvent;
import net.flintmc.mcapi.player.event.FieldOfViewEvent.Factory;

/**
 * Default implementation of the {@link FieldOfViewEvent}.
 */
@Singleton
@Implement(FieldOfViewModifier.class)
public class DefaultFieldOfViewModifier implements FieldOfViewModifier {

  private final EventBus eventBus;
  private final FieldOfViewEvent.Factory fieldOfViewEventFactory;

  private FieldOfViewEvent fieldOfViewEvent;

  @Inject
  private DefaultFieldOfViewModifier(EventBus eventBus, Factory fieldOfViewEventFactory) {
    this.eventBus = eventBus;
    this.fieldOfViewEventFactory = fieldOfViewEventFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float fieldOfView(float fov) {
    if (this.fieldOfViewEvent == null) {
      this.fieldOfViewEvent = this.fieldOfViewEventFactory.create(fov);
    } else {
      this.fieldOfViewEvent.setFov(fov);
    }

    this.eventBus.fireEvent(this.fieldOfViewEvent, Phase.PRE);

    // other logic

    this.eventBus.fireEvent(this.fieldOfViewEvent, Phase.POST);

    return this.fieldOfViewEvent.getFov();
  }
}
