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

package net.flintmc.mcapi.internal.chat.component;

import java.util.function.Function;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.component.KeybindComponent;

public class DefaultKeybindComponent extends DefaultChatComponent implements KeybindComponent {

  public static Function<Keybind, String> nameFunction = Keybind::getKey;

  private Keybind keybind;

  @Override
  public Keybind keybind() {
    return this.keybind;
  }

  @Override
  public void keybind(Keybind keybind) {
    this.keybind = keybind;
  }

  @Override
  public String getUnformattedText() {
    if (nameFunction == null) {
      nameFunction = Keybind::getKey;
    }

    return this.keybind == null ? "null" : nameFunction.apply(this.keybind);
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultKeybindComponent component = new DefaultKeybindComponent();
    component.keybind = this.keybind;
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.keybind == null;
  }
}
