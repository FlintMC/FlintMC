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

package net.flintmc.mcapi.settings.game.configuration;

import java.util.List;
import net.flintmc.framework.config.annotation.ConfigExclude;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.game.annotation.ResourcePackSetting;

/**
 * Represents the resource pack configuration.
 */
@ImplementedConfig
public interface ResourcePackConfiguration {

  /**
   * Retrieves a collection with all resource packs.<br>
   * <b>Note:</b> If you use this method to add a resource pack, the client only knows about it and
   * is temporary not saved in the options. The next time the options are saved, the added resource
   * packs are also saved.
   *
   * @return A collection with all resource packs.
   */
  @ResourcePackSetting
  @DisplayName(@Component(value = "options.resourcepack", translate = true))
  List<String> getResourcePacks();

  /**
   * Changes the old resource pack collection with the new collection.
   *
   * @param resourcePacks The new resource pack collection.
   */
  void setResourcePacks(List<String> resourcePacks);

  /**
   * Retrieves a collection with all incompatible resource packs.<br>
   * <b>Note:</b> If you use this method to add an incompatible resource pack, the client only
   * knows about it and is temporary not saved in the options. The next time the options are saved,
   * the added incompatible resource packs are also saved.
   *
   * @return A collection with all incompatible resource packs.
   */
  @ConfigExclude
  List<String> getIncompatibleResourcePacks();

  /**
   * Changes the old incompatible resource pack collection with the new collection.
   *
   * @param incompatibleResourcePacks The new incompatible resource pack collection.
   */
  @ConfigExclude
  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);
}
