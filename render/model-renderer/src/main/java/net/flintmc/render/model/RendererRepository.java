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

import java.util.function.Predicate;

public interface RendererRepository {

  /**
   * Gets an instance of the {@link Renderer} defined for a specific {@link RenderContextAware}. If
   * no {@link Renderable} was defined the default renderer will be returned. Otherwise if multiple
   * {@link Renderer}s would match the {@link Renderer}s defined with a {@link Predicate} will be
   * prioritized over all {@link Renderer}s defined with class reference.
   *
   * @param renderContextAware     the {@link RenderContextAware} to search the {@link Renderer} by.
   * @param <T_RenderContextAware> the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>      the type of the {@link RenderContext} associated with
   *                               renderContextAware. Just used for generic locking
   * @param <T_Renderable>         the type of the {@link Renderable} associated with renderContextAware.
   *                               Just used for generic locking
   * @param <T_RenderMeta>         the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>       the type of render target defined in {@link Renderable}. Just used for
   *                               generic locking
   * @return the {@link Renderer} defined for the {@link RenderContextAware}
   * @see #setRenderer(Class, Renderer)
   * @see #setRenderer(Predicate, Renderer)
   * @see #setRenderer(Predicate, Class)
   * @see #setRenderer(Class, Class)
   * @see #setDefaultRenderer(Renderer)
   * @see #getDefaultRenderer()
   * @see #appendRenderer(Renderer, Renderer)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  Renderer<T_Renderable, T_RenderContext, T_RenderMeta> getRenderer(
      T_RenderContextAware renderContextAware);

  /**
   * Sets the {@link Renderer} for all {@link RenderContext}s matching {@link
   * RenderContext#getOwner()} as {@link Class#isInstance(Object)} for renderContextAware. In order
   * for this action to take effect on {@link RenderContext} that already have discovered a {@link
   * Renderer} it is required to call {@link RenderContext#updateRenderer()}.
   *
   * @param renderContextAwareClass the type of the {@link RenderContextAware} to search the {@link
   *                                Renderer} by.
   * @param rendererClass           the type of the {@link Renderer} to set
   * @param <T_RenderContextAware>  the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>       the type of the {@link RenderContext} associated with
   *                                renderContextAware. Just used for generic locking
   * @param <T_Renderable>          the type of the {@link Renderable} associated with renderContextAware.
   *                                Just used for generic locking
   * @param <T_RenderMeta>          the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>        the type of render target defined in {@link Renderable}. Just used for
   *                                generic locking
   * @return this
   * @see #getRenderer(RenderContextAware)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> rendererClass);

  /**
   * Sets the {@link Renderer} for all {@link RenderContext}s matching {@link
   * RenderContext#getOwner()} as {@link Class#isInstance(Object)} for renderContextAware. In order
   * for this action to take effect on {@link RenderContext} that already have discovered a {@link
   * Renderer} it is required to call {@link RenderContext#updateRenderer()}.
   *
   * @param renderContextAwareClass the type of the {@link RenderContextAware} to search the {@link
   *                                Renderer} by.
   * @param renderer                the {@link Renderer} to set
   * @param <T_RenderContextAware>  the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>       the type of the {@link RenderContext} associated with
   *                                renderContextAware. Just used for generic locking
   * @param <T_Renderable>          the type of the {@link Renderable} associated with renderContextAware.
   *                                Just used for generic locking
   * @param <T_RenderMeta>          the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>        the type of render target defined in {@link Renderable}. Just used for
   *                                generic locking
   * @return this
   * @see #getRenderer(RenderContextAware)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer);

  /**
   * Sets the {@link Renderer} for all {@link RenderContext}s matching {@link
   * Predicate#test(Object)} for renderablePredicate. In order for this action to take effect on
   * {@link RenderContext} that already have discovered a {@link Renderer} it is required to call
   * {@link RenderContext#updateRenderer()}.
   *
   * @param renderablePredicate    the {@link Predicate} for {@link RenderContext} to search the {@link
   *                               Renderer} by.
   * @param rendererClass          the type of the {@link Renderer} to set
   * @param <T_RenderContextAware> the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>      the type of the {@link RenderContext} associated with
   *                               renderContextAware. Just used for generic locking
   * @param <T_Renderable>         the type of the {@link Renderable} associated with renderContextAware.
   *                               Just used for generic locking
   * @param <T_RenderMeta>         the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>       the type of render target defined in {@link Renderable}. Just used for
   *                               generic locking
   * @return this
   * @see #getRenderer(RenderContextAware)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?, ?>> renderablePredicate,
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> rendererClass);

  /**
   * Sets the {@link Renderer} for all {@link RenderContext}s matching {@link
   * Predicate#test(Object)} for renderablePredicate. In order for this action to take effect on
   * {@link RenderContext} that already have discovered a {@link Renderer} it is required to call
   * {@link RenderContext#updateRenderer()}.
   *
   * @param renderablePredicate    the {@link Predicate} for {@link RenderContext} to search the {@link
   *                               Renderer} by.
   * @param renderer               the {@link Renderer} to set
   * @param <T_RenderContextAware> the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>      the type of the {@link RenderContext} associated with
   *                               renderContextAware. Just used for generic locking
   * @param <T_Renderable>         the type of the {@link Renderable} associated with renderContextAware.
   *                               Just used for generic locking
   * @param <T_RenderMeta>         the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>       the type of render target defined in {@link Renderable}. Just used for
   *                               generic locking
   * @return this
   * @see #getRenderer(RenderContextAware)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?, ?>> renderablePredicate,
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer);

  /**
   * Appends an already set {@link Renderer} with another given {@link Renderer}. The old {@link
   * Renderer} will be called before the appendix. The old {@link
   * Renderer#shouldExecuteNextStage(Renderable, Object)} will be ignored. Insted the appended
   * {@link Renderer} will be used for that. In order for this action to take effect on {@link
   * RenderContext} that already have discovered a {@link Renderer} it is required to call {@link
   * RenderContext#updateRenderer()}.
   *
   * @param target                 the {@link Renderer} to append
   * @param appendix               the appending {@link Renderer}
   * @param <T_RenderContextAware> the type of renderContextAware. Just used for generic locking
   * @param <T_RenderContext>      the type of the {@link RenderContext} associated with
   *                               renderContextAware. Just used for generic locking
   * @param <T_Renderable>         the type of the {@link Renderable} associated with renderContextAware.
   *                               Just used for generic locking
   * @param <T_RenderMeta>         the type of render meta required by the target {@link Renderer}
   * @param <T_RenderTarget>       the type of render target defined in {@link Renderable}. Just used for
   *                               generic locking
   * @return this
   * @see #getRenderer(RenderContextAware)
   */
  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<
              T_RenderContextAware,
              T_RenderContext,
              T_Renderable,
              T_RenderMeta,
              T_RenderTarget>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
      T_RenderMeta,
      T_RenderTarget>
  RendererRepository appendRenderer(
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> target,
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> appendix);

  /**
   * @return the default generic {@link Renderer}. Will be null if not defined with {@link #setDefaultRenderer(Renderer)}
   */
  Renderer<?, ?, ?> getDefaultRenderer();

  /**
   * Sets the default generic {@link Renderer}.
   *
   * @param defaultRenderer the default generic {@link Renderer}
   * @return this
   */
  RendererRepository setDefaultRenderer(Renderer<?, ?, ?> defaultRenderer);
}
