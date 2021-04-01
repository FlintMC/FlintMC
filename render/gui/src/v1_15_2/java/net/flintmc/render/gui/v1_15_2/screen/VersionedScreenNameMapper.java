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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.screen.ScreenNameMapper;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.minecraft.client.gui.advancements.AdvancementsScreen;

@Singleton
@Implement(ScreenNameMapper.class)
public class VersionedScreenNameMapper implements ScreenNameMapper {

  // Map of all deobfuscated screen class names to their ScreenName equivalents
  private static final Map<String, ScreenName> KNOWN_NAMES = new HashMap<>();

  static {
    KNOWN_NAMES.put("net.minecraft.client.gui.advancements.AdvancementsScreen",
        ScreenName.minecraft(ScreenName.ADVANCEMENTS));

    KNOWN_NAMES.put("net.minecraft.client.gui.screen.MainMenuScreen",
        ScreenName.minecraft(ScreenName.MAIN_MENU));
    KNOWN_NAMES.put("net.minecraft.client.gui.screen.IngameMenuScreen",
        ScreenName.minecraft(ScreenName.INGAME_MENU));

    KNOWN_NAMES.put("net.minecraft.client.gui.screen.WorldSelectionScreen",
        ScreenName.minecraft(ScreenName.SINGLEPLAYER));
    KNOWN_NAMES.put("net.minecraft.client.gui.screen.WorldLoadProgressScreen",
        ScreenName.minecraft(ScreenName.WORLD_LOAD));
    KNOWN_NAMES.put("net.minecraft.client.gui.screen.MultiplayerScreen",
        ScreenName.minecraft(ScreenName.MULTIPLAYER));

    KNOWN_NAMES.put("net.minecraft.client.gui.ResourceLoadProgressGui",
        ScreenName.minecraft(ScreenName.RESOURCE_LOAD));
    KNOWN_NAMES.put("net.minecraft.client.gui.screen.OptionsScreen",
        ScreenName.minecraft(ScreenName.OPTIONS));

    KNOWN_NAMES.put("net.minecraft.client.gui.screen.ChatScreen",
        ScreenName.minecraft(ScreenName.CHAT));
    KNOWN_NAMES.put("net.flintmc.render.gui.v1_15_2.screen.VersionedDummyScreen",
        ScreenName.minecraft(ScreenName.DUMMY));
  }

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private VersionedScreenNameMapper(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScreenName fromClass(String className) {
    ScreenName screenName = KNOWN_NAMES.get(className);
    if (screenName == null) {
      ClassMapping mapping = this.classMappingProvider.get(className);
      if (mapping != null) {
        screenName = KNOWN_NAMES.get(mapping.getDeobfuscatedName());
      }
    }

    return screenName != null ? screenName : ScreenName.unknown(className);
  }
}
