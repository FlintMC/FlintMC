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

package net.flintmc.mcapi.internal.chat.event;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultHoverEvent implements HoverEvent {

  private final HoverContent[] contents;
  private List<ChatComponent> cachedText;

  protected DefaultHoverEvent(HoverContent... contents) {
    if (contents.length != 0) {
      Action action = contents[0].getAction();
      for (HoverContent content : contents) {
        if (action != content.getAction()) {
          throw new IllegalArgumentException("The action of every content needs to be the same");
        }
      }
    }
    this.contents = contents;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverContent[] getContents() {
    return this.contents;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ChatComponent> getAsText() {
    if (this.cachedText != null) {
      return this.cachedText;
    }

    List<ChatComponent> components = new ArrayList<>();

    for (HoverContent content : this.contents) {
      components.addAll(content.getAsText());
    }

    return this.cachedText = Collections.unmodifiableList(components);
  }
}
