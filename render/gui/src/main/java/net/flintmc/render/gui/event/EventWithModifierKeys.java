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

import java.util.Collections;
import java.util.Set;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.windowing.Window;

/**
 * Base class for events with key modifiers.
 */
@Subscribable(Phase.PRE)
public abstract class EventWithModifierKeys extends DefaultGuiEvent implements GuiEvent {

  protected final Set<ModifierKey> modifierKeys;

  /**
   * Constructs a new {@link EventWithModifierKeys} with the specified modifier keys active.
   *
   * @param window       The non-null window where this event has happened
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  protected EventWithModifierKeys(Window window, Set<ModifierKey> modifierKeys) {
    super(window);
    this.modifierKeys = modifierKeys;
  }

  /**
   * Retrieves a set of modifier keys which were active while the event was fired.
   *
   * @return An immutable set of modifier keys active during the event
   */
  public final Set<ModifierKey> getModifierKeys() {
    return Collections.unmodifiableSet(modifierKeys);
  }

  /**
   * Determines whether the shift key was down while the event was fired.
   *
   * @return {@code true} if the shift key was down, {@code false} otherwise
   */
  public boolean isShiftDown() {
    return modifierKeys.contains(ModifierKey.SHIFT);
  }

  /**
   * Determines whether the control key was down while the event was fired.
   *
   * @return {@code true} if the control key was down, {@code false} otherwise
   */
  public boolean isControlDown() {
    return modifierKeys.contains(ModifierKey.CONTROL);
  }

  /**
   * Determines whether the alt key was down while the event was fired.
   *
   * @return {@code true} if the alt key was down, {@code false} otherwise
   */
  public boolean isAltDown() {
    return modifierKeys.contains(ModifierKey.ALT);
  }

  /**
   * Determines whether the super key was down while the event was fired.
   *
   * @return {@code true} if the super key was down, {@code false} otherwise
   */
  public boolean isSuperDown() {
    return modifierKeys.contains(ModifierKey.SUPER);
  }
}
