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
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;

/**
 * Represents the mouse configuration.
 */
@DefineCategory(
    name = "minecraft.settings.mouse",
    displayName = @Component(value = "options.mouse_settings", translate = true))
@ImplementedConfig
public interface MouseConfiguration {

  /**
   * Retrieves the sensitivity of the mouse.
   *
   * @return The mouse sensitivity.
   */
  @SliderSetting(@Range(max = 200))
  // percent
  double getMouseSensitivity();

  /**
   * Changes the sensitivity of the mouse.
   *
   * @param mouseSensitivity The new mouse sensitivity.
   */
  void setMouseSensitivity(double mouseSensitivity);

  /**
   * Retrieves the sensitivity of the mouse wheel.
   *
   * @return The mouse wheel sensitivity.
   */
  @SliderSetting(value = @Range(min = 0.01, max = 10, decimals = 2))
  double getMouseWheelSensitivity();

  /**
   * Changes the sensitivity of the mouse wheel.
   *
   * @param mouseWheelSensitivity The new mouse wheel sensitivity.
   */
  void setMouseWheelSensitivity(double mouseWheelSensitivity);

  /**
   * Whether it is the raw mouse input.
   *
   * @return {@code true} if is the raw mouse input, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isRawMouseInput();

  /**
   * Changes the state of the raw mouse input.
   *
   * @param rawMouseInput The new state for the raw mouse input.
   */
  void setRawMouseInput(boolean rawMouseInput);

  /**
   * Whether the mouse inverted.
   *
   * @return {@code true} if the mouse is inverted, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isInvertMouse();

  /**
   * Changes the state of the inverted mouse.
   *
   * @param invertMouse The new state for the inverted mouse.
   */
  void setInvertMouse(boolean invertMouse);

  /**
   * Whether it is a discreet scrolling with the mouse.
   *
   * @return {@code true} if it is discreet scrolling with the mouse, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isDiscreteMouseScroll();

  /**
   * Changes the state of the discreet scrolling with the mouse.
   *
   * @param discreteMouseScroll The new state of for the discrete mouse scrolling.
   */
  void setDiscreteMouseScroll(boolean discreteMouseScroll);

  /**
   * Whether the touchscreen mode should be used.
   *
   * @return {@code true} if the touchscreen mode is used, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isTouchscreen();

  /**
   * Changes the touchscreen mode.
   *
   * @param touchscreen The new state for the touchscreen mode.
   */
  void setTouchscreen(boolean touchscreen);
}
