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

package net.flintmc.render.model.internal;

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.model.RenderContext;
import net.flintmc.render.model.RenderContextAware;
import net.flintmc.render.model.Renderable;
import net.flintmc.render.model.Renderer;
import net.flintmc.render.model.RendererRepository;

@Singleton
@Implement(RendererRepository.class)
public class DefaultRendererRepository implements RendererRepository {

  private final Map<Class<? extends RenderContextAware<?>>, Renderer<?, ?, ?>>
      classDefinedRenderers = new HashMap<>();
  private final Map<Predicate<RenderContext<?, ?, ?, ?, ?>>, Renderer<?, ?, ?>> predicates =
      new HashMap<>();
  private Renderer<?, ?, ?> defaultRenderer;

  @SuppressWarnings({"unchecked"})
  public <
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
      T_RenderContextAware renderContextAware) {
    return this.getRenderer(renderContextAware.getRenderContext());
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public <T_RenderContextAware extends RenderContextAware<T_RenderContext>, T_RenderContext extends RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta, T_RenderTarget>, T_Renderable extends Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>, T_RenderMeta, T_RenderTarget> Renderer<T_Renderable, T_RenderContext, T_RenderMeta> getRenderer(
      T_RenderContext renderContext) {

    Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer = null;
    for (Map.Entry<Predicate<RenderContext<?, ?, ?, ?, ?>>, Renderer<?, ?, ?>> entry :
        this.predicates.entrySet()) {
      Predicate<RenderContext<?, ?, ?, ?, ?>> predicate = entry.getKey();
      if (predicate.test(renderContext)) {
        if (renderer != null) {
          throw new IllegalStateException(
              String.format(
                  "Multiple default renderers for render context aware of type %s found.",
                  renderContext.getOwner().getClass()));
        }
        renderer = (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>) entry.getValue();
      }
    }
    if (renderer != null) {
      return renderer;
    }

    if (this.classDefinedRenderers.containsKey(renderContext.getOwner().getClass())) {
      return (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>)
          this.classDefinedRenderers.get(renderContext.getOwner().getClass());
    }

    for (Map.Entry<Class<? extends RenderContextAware<?>>, Renderer<?, ?, ?>> classRendererEntry :
        this.classDefinedRenderers.entrySet()) {
      if (classRendererEntry.getKey().isInstance(renderContext.getOwner())) {
        if (renderer != null) {
          throw new IllegalStateException(
              String.format(
                  "Multiple default renderers for render context aware of type %s found.",
                  renderContext.getOwner().getClass()));
        }
        renderer =
            (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>) classRendererEntry.getValue();
      }
    }

    if (renderer != null) {
      return renderer;
    }

    return (Renderer<T_Renderable, T_RenderContext, T_RenderMeta>) this.defaultRenderer;
  }

  public <
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
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> rendererClass) {
    return this.setRenderer(
        renderContextAwareClass, InjectionHolder.getInjectedInstance(rendererClass));
  }

  public <
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
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer) {
    if (this.classDefinedRenderers.containsKey(renderContextAwareClass)) {
      throw new IllegalStateException(
          "Renderer for render context aware "
              + renderContextAwareClass.getName()
              + " was already defined as "
              + this.classDefinedRenderers.get(renderContextAwareClass).getClass().getName());
    }

    this.classDefinedRenderers.put(renderContextAwareClass, renderer);
    return this;
  }

  public <
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
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> renderer) {
    return this.setRenderer(renderablePredicate, InjectionHolder.getInjectedInstance(renderer));
  }

  public <
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
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> renderer) {
    this.predicates.put(renderablePredicate, renderer);
    return this;
  }

  public <
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
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> appendix) {

    Renderer<T_Renderable, T_RenderContext, T_RenderMeta> appendedRenderer = new Renderer<T_Renderable, T_RenderContext, T_RenderMeta>() {
      public void render(T_Renderable renderable, T_RenderMeta renderMeta) {
        target.render(renderable, renderMeta);
        appendix.render(renderable, renderMeta);
      }

      public boolean shouldExecuteNextStage(T_Renderable renderable, T_RenderMeta renderMeta) {
        return appendix.shouldExecuteNextStage(renderable, renderMeta);
      }
    };

    for (Map.Entry<Class<? extends RenderContextAware<?>>, Renderer<?, ?, ?>> classRendererEntry :
        this.classDefinedRenderers.entrySet()) {
      if (classRendererEntry.getValue().equals(target)) {
        this.classDefinedRenderers.put(classRendererEntry.getKey(), appendedRenderer);
      }
    }

    if (target.equals(this.defaultRenderer)) {
      this.defaultRenderer = appendedRenderer;
    }

    for (Map.Entry<Predicate<RenderContext<?, ?, ?, ?, ?>>, Renderer<?, ?, ?>>
        predicateRendererEntry : this.predicates.entrySet()) {
      this.predicates.put(predicateRendererEntry.getKey(), appendedRenderer);
    }

    return this;
  }

  public Renderer<?, ?, ?> getDefaultRenderer() {
    return defaultRenderer;
  }

  public DefaultRendererRepository setDefaultRenderer(Renderer<?, ?, ?> defaultRenderer) {
    this.defaultRenderer = defaultRenderer;
    return this;
  }
}
