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

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.ScoreComponentBuilder;
import net.flintmc.mcapi.chat.component.ScoreComponent;
import net.flintmc.mcapi.chat.component.TextComponent;

@Implement(value = ScoreComponentBuilder.class)
public class DefaultScoreComponentBuilder
    extends DefaultComponentBuilder<ScoreComponentBuilder, ScoreComponent>
    implements ScoreComponentBuilder {

  @AssistedInject
  private DefaultScoreComponentBuilder(
      TextComponent.Factory textComponentFactory,
      ScoreComponent.Factory factory) {
    super(textComponentFactory, factory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScoreComponentBuilder name(String name) {
    super.currentComponent.name(name);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return super.currentComponent.name();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ScoreComponentBuilder objective(String objective) {
    super.currentComponent.objective(objective);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String objective() {
    return super.currentComponent.objective();
  }
}
