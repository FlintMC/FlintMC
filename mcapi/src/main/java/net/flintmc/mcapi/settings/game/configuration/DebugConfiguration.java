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

package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;

/**
 * Represents the debug configuration.
 */
@ImplementedConfig
public interface DebugConfiguration {

  /**
   * Whether debug information is displayed.
   *
   * @return {@code true} if debug information is displayed, otherwise {@code false}.
   */
  boolean isShowDebugInfo();

  /**
   * Changes the state whether the debug information is displayed.
   *
   * @param showDebugInfo The new state.
   */
  void setShowDebugInfo(boolean showDebugInfo);

  /**
   * Whether the debug profiler chart is displayed.
   *
   * @return {@code true} if the debug profiler chart is displayed, otherwise {@code false}.
   */
  boolean isShowDebugProfilerChart();

  /**
   * Changes the state whether the debug profiler chart is displayed.
   *
   * @param showDebugProfilerChart The new state.
   */
  void setShowDebugProfilerChart(boolean showDebugProfilerChart);

  /**
   * Whether the lagometer is displayed.
   *
   * @return {@code true} if the lagometer is displayed, otherwise {@code false}.
   */
  boolean isShowLagometer();

  /**
   * Changes the state whether the lagometer is displayed.
   *
   * @param showLagometer The new state.
   */
  void setShowLagometer(boolean showLagometer);

  /**
   * Retrieves the verbosity GL debug level.
   *
   * @return The verbosity GL debug level.
   */
  int getGlDebugVerbosity();

  /**
   * Changes the verbosity GL debug level.
   *
   * @param glDebugVerbosity The new verbosity GL debug level.
   */
  void setGlDebugVerbosity(int glDebugVerbosity);
}
