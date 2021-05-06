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

package net.flintmc.mcapi.player.skin;

import java.util.UUID;
import java.util.function.Consumer;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationWatcher;

public interface SkinProvider {

  ResourceLocationWatcher loadSkin(GameProfile gameProfile);

  ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile);

  ResourceLocationWatcher loadSkin(GameProfile gameProfile, Consumer<ResourceLocation> updateCallback);

  ResourceLocationWatcher loadHeadSkin(GameProfile gameProfile, Consumer<ResourceLocation> updateCallback);

}
