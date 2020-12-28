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

package net.flintmc.mcapi.internal.items.meta.enchantment;

import com.google.common.base.Preconditions;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.meta.enchantment.Enchantment;
import net.flintmc.mcapi.items.meta.enchantment.EnchantmentType;

@Implement(Enchantment.class)
public class DefaultEnchantment implements Enchantment {

  private final ComponentBuilder.Factory componentFactory;
  private final EnchantmentType type;
  private final int level;
  private ChatComponent displayName;

  @AssistedInject
  public DefaultEnchantment(
      ComponentBuilder.Factory componentFactory,
      @Assisted("type") EnchantmentType type,
      @Assisted("level") int level) {
    Preconditions.checkArgument(level > 0, "Level needs to be at least 1");
    this.componentFactory = componentFactory;
    this.type = type;
    this.level = level;
  }

  @Override
  public EnchantmentType getType() {
    return this.type;
  }

  @Override
  public int getLevel() {
    return this.level;
  }

  @Override
  public ChatComponent getDisplayName() {
    if (this.displayName != null) return this.displayName;

    ChatComponent displayName = this.type.getDisplayName().copy();

    if (this.level != 1 || this.type.getHighestLevel() != 1) {
      displayName.append(this.componentFactory.text().text(" ").build());
      displayName.append(
          this.componentFactory
              .translation()
              .translationKey("enchantment.level." + this.level)
              .build());
    }

    return this.displayName = displayName;
  }
}
