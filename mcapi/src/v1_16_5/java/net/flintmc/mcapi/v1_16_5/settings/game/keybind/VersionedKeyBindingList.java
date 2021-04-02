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

package net.flintmc.mcapi.v1_16_5.settings.game.keybind;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.settings.game.keybind.KeyBinding;
import net.flintmc.mcapi.settings.game.keybind.KeyBindingList;
import net.flintmc.mcapi.v1_16_5.settings.game.configuration.ShadowKeyBinding;
import net.flintmc.render.gui.input.Key;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(KeyBindingList.class)
public class VersionedKeyBindingList implements KeyBindingList {

  private final KeyBinding.Factory keyBindingFactory;

  @Inject
  private VersionedKeyBindingList(KeyBinding.Factory keyBindingFactory) {
    this.keyBindingFactory = keyBindingFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDuplicates(Key key) {
    boolean found = false;
    for (KeyBinding keyBinding : this.getKeyBindings()) {
      if (keyBinding.getKeyCode() == key.getKey()) {
        if (found) {
          return true;
        }
        found = true;
      }
    }
    for (KeyBinding keyBinding : this.getKeyBindsHotbar()) {
      if (keyBinding.getKeyCode() == key.getKey()) {
        if (found) {
          return true;
        }

        found = true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBinding getKeyBinding(Keybind keybind) {
    net.minecraft.client.settings.KeyBinding keyBinding =
        this.getMinecraftBinding(keybind.getKey());
    return keyBinding != null ? this.fromMinecraftObject(keyBinding) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindsHotbar() {
    return Arrays.stream(Minecraft.getInstance().gameSettings.keyBindsHotbar)
        .map(this::fromMinecraftObject)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<KeyBinding> getKeyBindings() {
    return Arrays.stream(Minecraft.getInstance().gameSettings.keyBindings)
        .map(this::fromMinecraftObject)
        .collect(Collectors.toList());
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

  private KeyBinding fromMinecraftObject(net.minecraft.client.settings.KeyBinding keyBinding) {
    return this.keyBindingFactory.create(
        keyBinding.getKeyDescription(),
        ((ShadowKeyBinding) keyBinding).getKeyCode().getKeyCode(),
        keyBinding.getKeyCategory());
  }
}
