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

import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;

public abstract class DefaultComponentBuilder<
    B extends ComponentBuilder<B>, C extends ChatComponent>
    implements ComponentBuilder<B> {

  private final TextComponent.Factory textComponentFactory;
  private final B builder;
  private final ChatComponent.Factory<C> factory;
  protected C currentComponent;
  private TextComponent baseComponent;

  @SuppressWarnings("unchecked")
  public DefaultComponentBuilder(
      TextComponent.Factory textComponentFactory, ChatComponent.Factory<C> factory) {
    this.textComponentFactory = textComponentFactory;
    this.factory = factory;
    this.currentComponent = factory.create();
    this.builder = (B) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent build() {
    if (this.baseComponent == null || this.baseComponent.extras().length == 0) {
      return this.currentComponent;
    }
    if (!this.currentComponent.isEmpty()) {
      this.baseComponent.append(this.currentComponent);
    }
    return this.baseComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B format(ChatFormat format) {
    this.currentComponent.toggleFormat(format, true);
    return this.builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatFormat[] enabledFormats() {
    return this.currentComponent.formats();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B color(ChatColor color) {
    this.currentComponent.color(color);
    return this.builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatColor color() {
    return this.currentComponent.color();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B clickEvent(ClickEvent event) {
    this.currentComponent.clickEvent(event);
    return this.builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClickEvent clickEvent() {
    return this.currentComponent.clickEvent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B hoverEvent(HoverEvent event) {
    this.currentComponent.hoverEvent(event);
    return this.builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HoverEvent hoverEvent() {
    return this.currentComponent.hoverEvent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B append(ChatComponent component) {
    this.currentComponent.append(component);
    return this.builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public B nextComponent() {
    if (this.currentComponent.isEmpty()) {
      return this.builder;
    }

    if (this.baseComponent == null) {
      this.baseComponent = this.textComponentFactory.create();
      this.baseComponent.text("");
    }
    this.baseComponent.append(this.currentComponent);
    this.currentComponent = this.factory.create();
    return this.builder;
  }
}
