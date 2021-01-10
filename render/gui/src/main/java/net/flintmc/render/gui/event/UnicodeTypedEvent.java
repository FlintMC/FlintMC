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

package net.flintmc.render.gui.event;

import java.util.Set;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.windowing.Window;

/**
 * Event indicating that the user has typed input
 */
@Subscribable(Phase.PRE)
public class UnicodeTypedEvent extends EventWithModifierKeys {

  private final int codepoint;

  /**
   * Constructs a new {@link UnicodeTypedEvent} with the given code point
   *
   * @param window       The non-null window where this event has happened
   * @param codepoint    The unicode code point the user has typed
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public UnicodeTypedEvent(Window window, int codepoint, Set<ModifierKey> modifierKeys) {
    super(window, modifierKeys);
    this.codepoint = codepoint;
  }

  /**
   * Retrieves the code point the user has typed
   *
   * @return Code point the user has typed
   */
  public int getCodepoint() {
    return codepoint;
  }
}
