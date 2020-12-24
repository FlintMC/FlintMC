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

package net.flintmc.util.session.internal.launcher;

import java.util.Collection;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.launcher.LauncherProfile;
import net.flintmc.util.session.launcher.LauncherProfiles;

@Implement(LauncherProfiles.class)
public class DefaultLauncherProfiles implements LauncherProfiles {

  private final String clientToken;
  private final int preferredVersion;
  private final Collection<LauncherProfile> profiles;

  @AssistedInject
  private DefaultLauncherProfiles(
      @Assisted("clientToken") String clientToken,
      @Assisted("preferredVersion") int preferredVersion,
      @Assisted("profiles") Collection<LauncherProfile> profiles) {
    this.clientToken = clientToken;
    this.preferredVersion = preferredVersion;
    this.profiles = profiles;
  }

  @Override
  public String getClientToken() {
    return this.clientToken;
  }

  @Override
  public int getPreferredVersion() {
    return this.preferredVersion;
  }

  @Override
  public Collection<LauncherProfile> getProfiles() {
    return this.profiles;
  }
}
