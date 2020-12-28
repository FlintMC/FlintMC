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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.util.session.ProfileLoader;

@Singleton
@Implement(ProfileLoader.class)
public class DefaultProfileLoader implements ProfileLoader {

  private final GameProfileSerializer<GameProfile> profileSerializer;
  private final MinecraftSessionService sessionService;

  @Inject
  private DefaultProfileLoader(GameProfileSerializer profileSerializer) {
    this.profileSerializer = profileSerializer;
    this.sessionService =
        new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString())
            .createMinecraftSessionService();
  }

  @Override
  public PropertyMap loadProfileProperties(UUID uniqueId) {
    return this.loadProfile(uniqueId).getProperties();
  }

  @Override
  public net.flintmc.mcapi.player.gameprofile.GameProfile loadProfile(UUID uniqueId) {
    GameProfile profile = new GameProfile(uniqueId, "Steve");
    this.sessionService.fillProfileProperties(profile, false);
    return this.profileSerializer.deserialize(profile);
  }
}
