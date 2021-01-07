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

package net.flintmc.mcapi.internal.world.generator.flat;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsHolder;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettingsSerializer;

@Singleton
@Implement(FlatWorldGeneratorSettings.Factory.class)
public class DefaultFlatWorldGeneratorSettingsFactory
    implements FlatWorldGeneratorSettings.Factory {

  private final Provider<FlatWorldGeneratorSettings> settingsProvider;
  private final FlatWorldGeneratorSettingsHolder holder;
  private final FlatWorldGeneratorSettingsSerializer serializer;

  @Inject
  private DefaultFlatWorldGeneratorSettingsFactory(
      Provider<FlatWorldGeneratorSettings> settingsProvider,
      FlatWorldGeneratorSettingsHolder holder,
      FlatWorldGeneratorSettingsSerializer serializer) {
    this.settingsProvider = settingsProvider;
    this.holder = holder;
    this.serializer = serializer;
  }

  @Override
  public FlatWorldGeneratorSettings createDefault() {
    return this.holder.createDefault();
  }

  @Override
  public FlatWorldGeneratorSettings createEmpty() {
    return this.settingsProvider.get();
  }

  @Override
  public FlatWorldGeneratorSettings parseString(String serialized) {
    return this.serializer.deserialize(serialized);
  }
}