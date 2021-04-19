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

package net.flintmc.mcapi.internal.settings.game.annotation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.resources.pack.ResourcePack;
import net.flintmc.mcapi.resources.pack.ResourcePackProvider;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.game.annotation.ResourcePackSetting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Singleton
@RegisterSettingHandler(ResourcePackSetting.class)
public class ResourcePackSettingHandler implements SettingHandler<ResourcePackSetting> {

  private final ResourcePackProvider provider;

  @Inject
  private ResourcePackSettingHandler(ResourcePackProvider provider) {
    this.provider = provider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    Collection<String> enabledNames = (Collection<String>) currentValue;
    Collection<ResourcePack> enabled = new ArrayList<>();
    Collection<ResourcePack> disabled = new ArrayList<>();
    Collection<ResourcePack> available = this.provider.getAvailable();
    for (ResourcePack pack : available) {
      (enabledNames.contains(pack.getName()) ? enabled : disabled).add(pack);
    }

    object.add("enabledPacks", this.mapPacks(enabled));
    object.add("disabledPacks", this.mapPacks(disabled));

    return object;
  }

  private JsonArray mapPacks(Collection<ResourcePack> packs) {
    JsonArray array = new JsonArray();

    for (ResourcePack pack : packs) {
      JsonObject object = new JsonObject();
      array.add(object);

      object.addProperty("name", pack.getName());
      object.addProperty("description", pack.getDescription());
    }

    return array;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidInput(Object input, RegisteredSetting setting) {
    if (!(input instanceof Collection)) {
      return false;
    }

    Collection<String> available =
        provider.getAvailable().stream().map(ResourcePack::getName).collect(Collectors.toSet());

    return available.containsAll((Collection<?>) input);
  }
}
