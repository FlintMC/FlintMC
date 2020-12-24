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

package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.Keybind;
import net.flintmc.mcapi.chat.builder.KeybindComponentBuilder;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultKeybindComponent;

@Implement(value = KeybindComponentBuilder.class)
public class DefaultKeybindComponentBuilder
    extends DefaultComponentBuilder<KeybindComponentBuilder, KeybindComponent>
    implements KeybindComponentBuilder {

  public DefaultKeybindComponentBuilder() {
    super(DefaultKeybindComponent::new);
  }

  @Override
  public KeybindComponentBuilder keybind(Keybind keybind) {
    super.currentComponent.keybind(keybind);
    return this;
  }

  @Override
  public Keybind keybind() {
    return super.currentComponent.keybind();
  }
}
