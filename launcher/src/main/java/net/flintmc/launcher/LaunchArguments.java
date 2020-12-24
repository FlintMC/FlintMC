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

package net.flintmc.launcher;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

/** POJ for JCommander */
@Parameters
public class LaunchArguments {
  @Parameter(
      names = {"--launch-target", "-t"},
      description = "Main class to launch, defaults to net.minecraft.client.Main")
  private String launchTarget = "net.minecraft.client.main.Main";

  // Collect all other arguments so we can pass them on to minecraft
  @Parameter private List<String> otherArguments = new ArrayList<>();

  /**
   * Retrieves the launch target which control should be handed over to after plugins have hooked
   * the launch process.
   *
   * @return The launch target which control should be handed over to
   */
  public String getLaunchTarget() {
    return launchTarget;
  }

  /**
   * Retrieves a list of arguments not processed by any commandline handler
   *
   * @return All unhandled arguments
   */
  public List<String> getOtherArguments() {
    return otherArguments;
  }
}
