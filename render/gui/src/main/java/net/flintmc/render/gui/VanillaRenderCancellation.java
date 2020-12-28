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

package net.flintmc.render.gui;

/** Represents a cancellable execution of rendering. */
public class VanillaRenderCancellation {
  private boolean shouldCancel;

  /** Constructs a new {@link VanillaRenderCancellation} which has not been cancelled. */
  public VanillaRenderCancellation() {
    this.shouldCancel = false;
  }

  /**
   * Constructs a new {@link VanillaRenderCancellation} which cancellation depends on the given
   * parameter.
   *
   * @param isCancelled Whether the execution has been cancelled already
   */
  public VanillaRenderCancellation(boolean isCancelled) {
    this.shouldCancel = isCancelled;
  }

  /** Signal the cancellation */
  public void cancel() {
    this.shouldCancel = true;
  }

  /**
   * Determines whether the cancellation has been signaled
   *
   * @return {@code true} if the cancellation has been signaled, {@code false} otherwise
   */
  public boolean isCancelled() {
    return shouldCancel;
  }
}
