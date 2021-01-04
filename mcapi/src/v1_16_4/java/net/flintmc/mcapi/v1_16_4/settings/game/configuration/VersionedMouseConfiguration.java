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
import net.flintmc.mcapi.settings.game.configuration.MouseConfiguration;
import net.minecraft.client.Minecraft;

/**
 * 1.16.4 implementation of the {@link MouseConfiguration}.
 */
@Singleton
@ConfigImplementation(value = MouseConfiguration.class, version = "1.16.4")
public class VersionedMouseConfiguration implements MouseConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMouseSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseSensitivity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMouseSensitivity(double mouseSensitivity) {
    Minecraft.getInstance().gameSettings.mouseSensitivity = mouseSensitivity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMouseWheelSensitivity() {
    return Minecraft.getInstance().gameSettings.mouseWheelSensitivity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMouseWheelSensitivity(double mouseWheelSensitivity) {
    Minecraft.getInstance().gameSettings.mouseWheelSensitivity = mouseWheelSensitivity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRawMouseInput() {
    return Minecraft.getInstance().gameSettings.rawMouseInput;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRawMouseInput(boolean rawMouseInput) {
    Minecraft.getInstance().gameSettings.rawMouseInput = rawMouseInput;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvertMouse() {
    return Minecraft.getInstance().gameSettings.invertMouse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInvertMouse(boolean invertMouse) {
    Minecraft.getInstance().gameSettings.invertMouse = invertMouse;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDiscreteMouseScroll() {
    return Minecraft.getInstance().gameSettings.discreteMouseScroll;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDiscreteMouseScroll(boolean discreteMouseScroll) {
    Minecraft.getInstance().gameSettings.discreteMouseScroll = discreteMouseScroll;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTouchscreen() {
    return Minecraft.getInstance().gameSettings.touchscreen;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTouchscreen(boolean touchscreen) {
    Minecraft.getInstance().gameSettings.touchscreen = touchscreen;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
