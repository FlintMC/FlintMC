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

package net.flintmc.render.model;

/**
 * Defines the component in the rendering chain to draw a {@link Renderable} to the current opengl
 * context
 *
 * @param <T_Renderable>    the type of the {@link Renderable} to handle.
 * @param <T_RenderContext> the component that coordinates the rendering process.
 * @param <T_RenderMeta>    the type of metadata that must be provided to this {@link Renderer}.
 */
public interface Renderer<
    T_Renderable extends Renderable<?, ?, T_Renderable, ?>,
    T_RenderContext extends RenderContext<?, ?, T_Renderable, T_RenderMeta, ?>,
    T_RenderMeta> {

  /**
   * Render the given {@link Renderable}. The context in which this will be called depends on the
   * implementation of the underlying layers. All Flint implementations will always provide a valid
   * OpenGL State for the target {@link Renderable}.
   *
   * @param renderable the {@link Renderable} that should be rendered
   * @param renderMeta the metadata required to render the renderable. If not needed, use {@link
   *                   Void}
   */
  void render(T_Renderable renderable, T_RenderMeta renderMeta);

  /**
   * Defines if this {@link Renderer} ends the current render round.
   *
   * @param renderable the {@link Renderable} that should be rendered
   * @param renderMeta the metadata required to render the renderable. If not needed, use {@link
   *                   Void}
   * @return this
   */
  boolean shouldExecuteNextStage(T_Renderable renderable, T_RenderMeta renderMeta);
}
