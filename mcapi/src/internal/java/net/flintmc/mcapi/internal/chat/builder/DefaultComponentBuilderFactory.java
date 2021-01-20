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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.builder.KeybindComponentBuilder;
import net.flintmc.mcapi.chat.builder.ScoreComponentBuilder;
import net.flintmc.mcapi.chat.builder.SelectorComponentBuilder;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder.Factory;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;

@Singleton
@Implement(ComponentBuilder.Factory.class)
public class DefaultComponentBuilderFactory implements ComponentBuilder.Factory {

  private final TextComponentBuilder.Factory textFactory;
  private final KeybindComponentBuilder.Factory keybindFactory;
  private final ScoreComponentBuilder.Factory scoreFactory;
  private final SelectorComponentBuilder.Factory selectorFactory;
  private final TranslationComponentBuilder.Factory translationFactory;

  @Inject
  private DefaultComponentBuilderFactory(
      Factory textFactory,
      KeybindComponentBuilder.Factory keybindFactory,
      ScoreComponentBuilder.Factory scoreFactory,
      SelectorComponentBuilder.Factory selectorFactory,
      TranslationComponentBuilder.Factory translationFactory) {
    this.textFactory = textFactory;
    this.keybindFactory = keybindFactory;
    this.scoreFactory = scoreFactory;
    this.selectorFactory = selectorFactory;
    this.translationFactory = translationFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextComponentBuilder text() {
    return this.textFactory.newBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeybindComponentBuilder keybind() {
    return this.keybindFactory.newBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScoreComponentBuilder score() {
    return this.scoreFactory.newBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SelectorComponentBuilder selector() {
    return this.selectorFactory.newBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TranslationComponentBuilder translation() {
    return this.translationFactory.newBuilder();
  }
}
