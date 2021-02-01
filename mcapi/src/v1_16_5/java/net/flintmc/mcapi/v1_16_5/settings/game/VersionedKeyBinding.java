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

package net.flintmc.mcapi.v1_16_5.settings.game;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.game.KeyBinding;
import net.flintmc.mcapi.v1_16_5.settings.game.configuration.ShadowKeyBinding;
import net.flintmc.render.gui.input.Key;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

/**
 * 1.16.5 implementation of {@link KeyBinding}.
 */
@Implement(value = KeyBinding.class, version = "1.16.5")
public class VersionedKeyBinding extends net.minecraft.client.settings.KeyBinding
    implements KeyBinding {

  @AssistedInject
  private VersionedKeyBinding(
      @Assisted("description") String description,
      @Assisted("keyCode") int keyCode,
      @Assisted("category") String category) {
    super(description, keyCode, category);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getKeyCode() {
    return ((ShadowKeyBinding) this).getKeyCode().getKeyCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(Key key) {
    super.bind(InputMappings.getInputByName(key.getConfigurationName()));
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocalizedName() {
    return this.getTranslationKey();
  }
}
