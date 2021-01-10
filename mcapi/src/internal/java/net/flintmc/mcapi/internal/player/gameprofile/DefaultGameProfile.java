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

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

/**
 * An implementation of the {@link GameProfile}
 */
@Implement(GameProfile.class)
public class DefaultGameProfile implements GameProfile {

  /**
   * The unique identifier of this profile.
   */
  private final UUID uniqueId;
  /**
   * The display name of this profile.
   */
  private final String name;
  /**
   * The properties of this profile.
   */
  private final PropertyMap properties;
  /**
   * If this profile is a legacy.
   */
  private boolean legacy;

  @AssistedInject
  private DefaultGameProfile(@Assisted("uniqueId") UUID uniqueId, @Assisted("name") String name) {
    this(uniqueId, name, InjectionHolder.getInjectedInstance(PropertyMap.Factory.class).create());
  }

  @AssistedInject
  private DefaultGameProfile(
      @Assisted("uniqueId") UUID uniqueId,
      @Assisted("name") String name,
      @Assisted("properties") PropertyMap properties) {
    if (uniqueId == null && isBlank(name)) {
      throw new IllegalArgumentException("Name and identifier cannot both be blank");
    }
    this.uniqueId = uniqueId;
    this.name = name;
    this.properties = properties;
  }

  /**
   * Retrieves the unique identifier of this game profile. This may be null for partial profile data
   * if constructed manually.
   *
   * @return The unique identifier of the profile
   */
  @Override
  public UUID getUniqueId() {
    return this.uniqueId;
  }

  /**
   * Retrieves the display name of this game profile. This may be null for partial profile data if
   * constructed manually.
   *
   * @return The name of the profile
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Retrieves any known properties about this game profile.
   *
   * @return A modifiable map of profile properties.
   */
  @Override
  public PropertyMap getProperties() {
    return this.properties;
  }

  /**
   * Whether this profile is complete. A complete profile has no empty fields. Partial profiles my
   * be constructed manually and used as input to methods.
   *
   * @return {@code true} if this profile is complete (as opposed to partial), otherwise {@code
   * false}
   */
  @Override
  public boolean isComplete() {
    return this.uniqueId != null && !this.isBlank(this.name);
  }

  /**
   * Whether this profile is legacy.
   *
   * @return {@code true} if this profile is legacy, otherwise {@code false}
   */
  @Override
  public boolean isLegacy() {
    return this.legacy;
  }

  /**
   * Whether if a {@link CharSequence} is empty (""), {@code null} or whitespace only.
   *
   * @param sequence The {@link CharSequence} to check, may be {@code null}
   * @return {@code true} if the {@link CharSequence} is null, empty or whitespace only
   */
  private boolean isBlank(CharSequence sequence) {
    int length = sequence.length();
    if (length == 0) {
      return true;
    }

    for (int i = 0; i < length; i++) {
      if (!Character.isWhitespace(sequence.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
