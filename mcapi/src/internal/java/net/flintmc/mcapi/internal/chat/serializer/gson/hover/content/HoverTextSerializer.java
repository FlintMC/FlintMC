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

package net.flintmc.mcapi.internal.chat.serializer.gson.hover.content;

import com.google.gson.Gson;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;
import net.flintmc.mcapi.chat.component.event.content.HoverText;

/** Serializer for {@link HoverText} */
public class HoverTextSerializer implements HoverContentSerializer {

  @Override
  public HoverContent deserialize(
      ChatComponent component, ComponentBuilder.Factory componentFactory, Gson gson) {
    return new HoverText(component);
  }

  @Override
  public ChatComponent serialize(
      HoverContent content, ComponentBuilder.Factory componentFactory, Gson gson) {
    HoverText text = (HoverText) content;

    return text.getText();
  }
}
