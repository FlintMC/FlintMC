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

package net.flintmc.render.gui.v1_15_2.screen;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.screen.ScreenNameMapper;

import java.util.Map;

@Singleton
@Implement(value = ScreenNameMapper.class, version = "1.15.2")
public class VersionedScreenNameMapper implements ScreenNameMapper {
  // Map of all deobfuscated screen class names to their ScreeName equivalents
  private static final Map<String, ScreenName> KNOWN_NAMES =
      ImmutableMap.of(
          "net.minecraft.client.gui.screen.MainMenuScreen",
              ScreenName.minecraft(ScreenName.MAIN_MENU),
          "net.minecraft.client.gui.ResourceLoadProgressGui",
              ScreenName.minecraft(ScreenName.RESOURCE_LOAD),
          "net.minecraft.client.gui.screen", ScreenName.minecraft(ScreenName.OPTIONS),
          "net.minecraft.client.gui.screen.MultiplayerScreen",
              ScreenName.minecraft(ScreenName.MULTIPLAYER),
          "net.minecraft.client.gui.screen.WorldSelectionScreen",
              ScreenName.minecraft(ScreenName.SINGLEPLAYER));

  /** {@inheritDoc} */
  @Override
  public ScreenName fromClass(String className) {
    return KNOWN_NAMES.get(className);
  }
}
