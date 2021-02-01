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

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.util.i18n.I18n;

@Implement(KeybindComponent.class)
public class DefaultKeybindComponent extends DefaultChatComponent implements KeybindComponent {

  private final I18n i18n;
  private final KeybindNameMapper mapper;

  private Keybind keybind;

  @AssistedInject
  private DefaultKeybindComponent(I18n i18n, KeybindNameMapper mapper) {
    this.i18n = i18n;
    this.mapper = mapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Keybind keybind() {
    return this.keybind;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void keybind(Keybind keybind) {
    this.keybind = keybind;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnformattedText() {
    return this.keybind == null ? "null" : this.mapper.translateKeybind(this.keybind);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected DefaultChatComponent createCopy() {
    DefaultKeybindComponent component = new DefaultKeybindComponent(i18n, mapper);
    component.keybind = this.keybind;
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isSpecificEmpty() {
    return this.keybind == null;
  }
}
