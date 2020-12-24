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

package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.internal.chat.component.DefaultTextComponent;

@Implement(value = TextComponentBuilder.class)
public class DefaultTextComponentBuilder
    extends DefaultComponentBuilder<TextComponentBuilder, TextComponent>
    implements TextComponentBuilder {

  public DefaultTextComponentBuilder() {
    super(DefaultTextComponent::new);
  }

  @Override
  public TextComponentBuilder text(String text) {
    super.currentComponent.text(text);
    return this;
  }

  @Override
  public TextComponentBuilder appendText(String text) {
    String currentText = this.text();
    return this.text(currentText == null ? text : currentText + text);
  }

  @Override
  public String text() {
    return super.currentComponent.text();
  }
}
