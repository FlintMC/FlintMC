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

package net.flintmc.render.shader;

import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.shader.uniformprovider.PartialTickProvider;
import net.flintmc.render.shader.uniformprovider.ProjectionMatrixProvider;
import net.flintmc.render.shader.uniformprovider.WorldMatrixProvider;

/**
 * Lists automatic uniform value provider implemented in Flint.
 */
public enum UniformProvider implements ShaderUniformProvider {
  /**
   * Updates a uniform with the current partial tick.
   */
  PARTIAL_TICKS(InjectionHolder.getInjectedInstance(PartialTickProvider.class)),
  /**
   * Updates a uniform with the current projection matrix.
   */
  PROJECTION_MATRIX(InjectionHolder.getInjectedInstance(ProjectionMatrixProvider.class)),
  /**
   * Updates a uniform with the current world matrix.
   */
  WORLD_MATRIX(InjectionHolder.getInjectedInstance(WorldMatrixProvider.class));

  private final ShaderUniformProvider provider;

  UniformProvider(ShaderUniformProvider provider) {
    this.provider = provider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void apply(ShaderUniform uniform) {
    provider.apply(uniform);
  }
}
