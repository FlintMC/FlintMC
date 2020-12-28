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

package net.flintmc.mcapi.internal.settings.flint.registered;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;

/**
 * {@inheritDoc}
 */
@Implement(RegisteredCategory.class)
public class DefaultRegisteredCategory implements RegisteredCategory {

  private final String registryName;
  private final ChatComponent displayName;
  private final ChatComponent description;
  private final Icon icon;

  @AssistedInject
  public DefaultRegisteredCategory(
      @Assisted("registryName") String registryName,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("description") ChatComponent description,
      @Assisted("icon") Icon icon) {
    this.registryName = registryName;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRegistryName() {
    return this.registryName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDescription() {
    return this.description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return this.icon;
  }
}
