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

package net.flintmc.mcapi.internal.player.gameprofile;

import com.google.inject.Inject;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

/**
 * An implementation of {@link GameProfile.Builder}
 */
@Implement(GameProfile.Builder.class)
public class DefaultGameProfileBuilder implements GameProfile.Builder {

  private final GameProfile.Factory gameProfileFactory;

  private UUID uniqueId;
  private String name;
  private PropertyMap properties;

  @Inject
  private DefaultGameProfileBuilder(GameProfile.Factory gameProfileFactory) {
    this.gameProfileFactory = gameProfileFactory;
  }

  /**
   * Sets the unique identifier for this game profile
   *
   * @param uniqueId The unique identifier of this game profile
   * @return This builder, for chaining
   */
  @Override
  public GameProfile.Builder setUniqueId(UUID uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  /**
   * Sets the display name for this game profile
   *
   * @param name The display name of this game profile
   * @return This builder, for chaining
   */
  @Override
  public GameProfile.Builder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the properties for this game profile
   *
   * @param properties The game profile properties
   * @return This builder, for chaining
   */
  @Override
  public GameProfile.Builder setProperties(PropertyMap properties) {
    this.properties = properties;
    return this;
  }

  /**
   * Built the game profile
   *
   * @return The built game profile
   */
  @Override
  public GameProfile build() {
    return this.properties == null
        ? this.gameProfileFactory.create(this.uniqueId, this.name)
        : this.gameProfileFactory.create(this.uniqueId, this.name, this.properties);
  }
}
