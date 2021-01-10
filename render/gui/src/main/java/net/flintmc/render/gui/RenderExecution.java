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

/**
 * Represents an execution of a rendering cycle.
 */
public class RenderExecution {

  private final float partialTicks;
  private final VanillaRenderCancellation cancellation;

  /**
   * Constructs a new {@link RenderExecution} with the given mouse x and y coordinates, the current
   * partial ticks value and an internal cancellation.
   *
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(float partialTicks) {
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation();
  }

  /**
   * Constructs a new {@link RenderExecution} with a boolean determining the the cancellation is
   * canceled already, the given mouse x and y coordinate and the current partial ticks value.
   *
   * @param isCancelled  Whether the execution has been cancelled already
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(boolean isCancelled, float partialTicks) {
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation(isCancelled);
  }

  /**
   * Retrieves the value of partial ticks at the start of the execution
   *
   * @return The partial ticks counter value
   */
  public float getPartialTicks() {
    return partialTicks;
  }

  /**
   * Retrieves the cancellation bound to this execution
   *
   * @return The cancellation bound to this execution
   */
  public VanillaRenderCancellation getCancellation() {
    return cancellation;
  }
}
