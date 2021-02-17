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

import java.util.function.Consumer;
import net.flintmc.util.property.PropertyContextAware;

/**
 * Defines an abstract component that can be rendered.
 *
 * @param <T_RenderContextAware> the component that is the "owner" of the provided render context.
 * @param <T_RenderContext>      the component that coordinates the rendering process.
 * @param <T_Renderable>         the type of this component. Just used here to lock the generic types of the
 *                               system to ensure type safety.
 * @param <T_RenderTarget>       the type of metadata that can be attached to the component.
 */
public interface Renderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, ?, T_RenderTarget>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget>
    extends PropertyContextAware<T_Renderable> {

  /** @return the target attached to this renderable component */
  T_RenderTarget getTarget();

  /** @return the component that coordinates the rendering process */
  T_RenderContext getContext();

  /**
   * Calls all pre-render hooks that has been added with {@link
   * Renderable#addRenderPreparation(Consumer)}. Should not be used to set any properties before the
   * render. For this @see {@link Renderable#callPropertyHandler()}.
   *
   * @return this
   */
  T_Renderable callRenderPreparations();

  Consumer<T_Renderable> getPropertyHandler();

  /**
   * Calls the handler that is used only to set properties.
   *
   * @return this
   */
  T_Renderable callPropertyHandler();

  /**
   * Calls the handler that is used to prepare the property default values.
   *
   * @return this
   */
  T_Renderable callPropertyPreparations();

  /**
   * Calls the handler that is used to cleanup the state after the render
   *
   * @return this
   */
  T_Renderable callRenderCleanup();

  /**
   * Adds a pre-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callRenderPreparations()}. Should not be used to set any properties before the
   * render. For this @see {@link Renderable#setPropertyHandler(Consumer)}.
   *
   * @param consumer the hook to add
   * @return this
   */
  T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer);

  /**
   * Adds a pre-property-handler hook that can be called - usually by the {@link Renderer} - with
   * {@link Renderable#callPropertyPreparations()}.
   *
   * @param consumer the hook to add
   * @return this
   */
  T_Renderable addPropertyPreparation(Consumer<T_Renderable> consumer);

  /**
   * Adds a post-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callRenderCleanup()} ()}.
   *
   * @param consumer the hook to add
   * @return this
   */
  T_Renderable addRenderCleanup(Consumer<T_Renderable> consumer);

  /**
   * Sets the pre-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callPropertyHandler()}. Should not be used to set anything else than properties
   * before the render. For this @see {@link Renderable#addRenderPreparation(Consumer)} (Consumer)}.
   *
   * @param consumer the new property handler
   * @return this
   */
  T_Renderable setPropertyHandler(Consumer<T_Renderable> consumer);
}
