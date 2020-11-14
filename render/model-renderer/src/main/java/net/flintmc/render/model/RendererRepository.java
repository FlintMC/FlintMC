package net.flintmc.render.model;

import java.util.function.Predicate;

public interface RendererRepository {

  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_RenderMeta>
  Renderer<T_Renderable, T_RenderContext> getRenderer(T_RenderContextAware renderContextAware);

  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_RenderMeta>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Class<? extends Renderer<T_Renderable, T_RenderContext>> rendererClass);

  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_RenderMeta>
  RendererRepository setRenderer(
      Class<? extends T_RenderContextAware> renderContextAwareClass,
      Renderer<T_Renderable, T_RenderContext> renderer);

  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_RenderMeta>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?>> renderablePredicate,
      Class<? extends Renderer<T_Renderable, T_RenderContext>> renderer);

  <
      T_RenderContextAware extends RenderContextAware<T_RenderContext>,
      T_RenderContext extends
          RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_Renderable extends
          Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
      T_RenderMeta>
  RendererRepository setRenderer(
      Predicate<RenderContext<?, ?, ?, ?>> renderablePredicate,
      Renderer<T_Renderable, T_RenderContext> renderer);

  Renderer<?, ?> getDefaultRenderer();

  RendererRepository setDefaultRenderer(Renderer<?, ?> defaultRenderer);
}
