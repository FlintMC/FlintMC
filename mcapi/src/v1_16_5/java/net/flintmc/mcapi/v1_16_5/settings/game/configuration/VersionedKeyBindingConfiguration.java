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

package net.flintmc.mcapi.v1_16_5.settings.game.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.settings.game.configuration.KeyBindingConfiguration;
import net.flintmc.render.gui.input.Key;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.16.5 implementation of {@link KeyBindingConfiguration}.
 */
@Singleton
@ConfigImplementation(KeyBindingConfiguration.class)
public class VersionedKeyBindingConfiguration implements KeyBindingConfiguration {

  @Inject
  private VersionedKeyBindingConfiguration() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Key getKey(String keyDescription) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    return keyBinding != null
        ? Key.getByConfigurationName(
        ((ShadowKeyBinding) keyBinding).getKeyCode().getTranslationKey())
        : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKey(String keyDescription, Key key) {
    net.minecraft.client.settings.KeyBinding keyBinding = this.getMinecraftBinding(keyDescription);
    if (keyBinding != null) {
      keyBinding.bind(
          key == null
              ? InputMappings.INPUT_INVALID
              : InputMappings.getInputByName(key.getConfigurationName()));
      Minecraft.getInstance().gameSettings.saveOptions();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Key> getAllKey() {
    Map<String, Key> keys = new HashMap<>();
    for (net.minecraft.client.settings.KeyBinding keyBinding :
        Minecraft.getInstance().gameSettings.keyBindings) {
      keys.put(
          keyBinding.getKeyDescription(),
          Key.getByConfigurationName(
              ((ShadowKeyBinding) keyBinding).getKeyCode().getTranslationKey()));
    }
    return keys;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllKey(Map<String, Key> keys) {
    keys.forEach(this::setKey);
  }

  private net.minecraft.client.settings.KeyBinding getMinecraftBinding(String keyDescription) {
    for (net.minecraft.client.settings.KeyBinding keyBinding :
        Minecraft.getInstance().gameSettings.keyBindings) {
      if (keyBinding.getKeyDescription().equals(keyDescription)) {
        return keyBinding;
      }
    }

    return null;
  }
}
