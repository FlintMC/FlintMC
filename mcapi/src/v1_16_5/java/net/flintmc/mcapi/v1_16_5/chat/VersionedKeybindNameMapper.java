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

package net.flintmc.mcapi.v1_16_5.chat;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.internal.chat.component.KeybindNameMapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@Implement(value = KeybindNameMapper.class, version = "1.16.5")
public class VersionedKeybindNameMapper implements KeybindNameMapper {

  /**
   * {@inheritDoc}
   */
  @Override
  public String translateKeybind(Keybind keybind) {
    ITextComponent component = KeyBinding.getDisplayString(keybind.getKey()).get();
    if (component instanceof TranslationTextComponent) {
      String key = ((TranslationTextComponent) component).getKey();
      Object[] args = ((TranslationTextComponent) component).getFormatArgs();

      return I18n.format(key, args);
    }

    return component.getUnformattedComponentText();
  }
}
