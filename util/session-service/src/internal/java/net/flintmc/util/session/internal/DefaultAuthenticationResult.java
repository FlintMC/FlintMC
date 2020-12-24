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

package net.flintmc.util.session.internal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.util.session.AuthenticationResult;

@Implement(AuthenticationResult.class)
public class DefaultAuthenticationResult implements AuthenticationResult {

  private final Type type;
  private final GameProfile profile;

  @AssistedInject
  private DefaultAuthenticationResult(@Assisted("type") Type type) { // failed
    this(type, null);
  }

  @AssistedInject
  private DefaultAuthenticationResult(@Assisted("profile") GameProfile profile) { // success
    this(Type.SUCCESS, profile);
  }

  private DefaultAuthenticationResult(Type type, GameProfile profile) {
    this.type = type;
    this.profile = profile;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public GameProfile getProfile() {
    return this.profile;
  }
}
