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

package net.flintmc.mcapi.internal.chat.component;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.TextComponent;

@Implement(TextComponent.class)
public class DefaultTextComponent extends DefaultChatComponent implements TextComponent {

  private String text;

  @AssistedInject
  private DefaultTextComponent() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String text() {
    return this.text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void text(String text) {
    this.text = text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnformattedText() {
    return this.text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected DefaultChatComponent createCopy() {
    DefaultTextComponent component = new DefaultTextComponent();
    component.text = this.text;
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isSpecificEmpty() {
    return this.text == null;
  }
}
