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

package net.flintmc.mcapi.settings.flint;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * This exception will be thrown when an entry in a {@link Config} is being discovered as a setting
 * but the {@link ConfigObjectReference#getSerializedType()} and {@link ApplicableSetting#types()}
 * don't match.
 */
public class InvalidSettingsException extends RuntimeException {

  /**
   * {@inheritDoc}
   */
  public InvalidSettingsException(String message) {
    super(message);
  }

  /**
   * {@inheritDoc}
   */
  public InvalidSettingsException(String message, Throwable cause) {
    super(message, cause);
  }
}
