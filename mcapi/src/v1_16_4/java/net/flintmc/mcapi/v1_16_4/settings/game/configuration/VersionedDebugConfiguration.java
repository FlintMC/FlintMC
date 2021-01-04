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

package net.flintmc.mcapi.v1_16_4.settings.game.configuration;

import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.settings.game.configuration.DebugConfiguration;
import net.minecraft.client.Minecraft;

/**
 * 1.16.4 implementation of {@link DebugConfiguration}.
 */
@Singleton
@ConfigImplementation(value = DebugConfiguration.class, version = "1.16.4")
public class VersionedDebugConfiguration implements DebugConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGlDebugVerbosity() {
    return Minecraft.getInstance().gameSettings.glDebugVerbosity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGlDebugVerbosity(int glDebugVerbosity) {
    Minecraft.getInstance().gameSettings.glDebugVerbosity = glDebugVerbosity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDebugInfo() {
    return Minecraft.getInstance().gameSettings.showDebugInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDebugInfo(boolean showDebugInfo) {
    Minecraft.getInstance().gameSettings.showDebugInfo = showDebugInfo;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDebugProfilerChart() {
    return Minecraft.getInstance().gameSettings.showDebugProfilerChart;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDebugProfilerChart(boolean showDebugProfilerChart) {
    Minecraft.getInstance().gameSettings.showDebugProfilerChart = showDebugProfilerChart;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowLagometer() {
    return Minecraft.getInstance().gameSettings.showLagometer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowLagometer(boolean showLagometer) {
    Minecraft.getInstance().gameSettings.showLagometer = showLagometer;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
