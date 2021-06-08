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

package net.flintmc.mcapi.v1_15_2.chat.component.event.content;

import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent.Action;
import net.flintmc.mcapi.chat.component.event.content.HoverText;

public class VersionedHoverText implements HoverText {

  private final ChatComponent component;

  @AssistedInject
  private VersionedHoverText(@Assisted ChatComponent component) {
    this.component = component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Action getAction() {
    return Action.SHOW_TEXT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ChatComponent> getAsText() {
    return Collections.singletonList(this.component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getText() {
    return this.component;
  }

  @AssistedFactory(VersionedHoverText.class)
  interface VersionedFactory {

    VersionedHoverText create(@Assisted ChatComponent component);
  }
}
