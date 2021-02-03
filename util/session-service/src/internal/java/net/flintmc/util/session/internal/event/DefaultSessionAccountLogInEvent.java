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

package net.flintmc.util.session.internal.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.event.SessionAccountLogInEvent;

@Implement(SessionAccountLogInEvent.class)
public class DefaultSessionAccountLogInEvent implements SessionAccountLogInEvent {

  private final GameProfile previousProfile;
  private final GameProfile newProfile;

  @AssistedInject
  private DefaultSessionAccountLogInEvent(
      @Assisted("previousProfile") GameProfile previousProfile,
      @Assisted("newProfile") GameProfile newProfile) {
    this.previousProfile = previousProfile;
    this.newProfile = newProfile;
  }

  @AssistedInject
  public DefaultSessionAccountLogInEvent(@Assisted("newProfile") GameProfile newProfile) {
    this(null, newProfile);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getPreviousProfile() {
    return this.previousProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getNewProfile() {
    return this.newProfile;
  }
}
