package net.flintmc.render.model;

import java.util.function.Predicate;

public interface RendererRepository {

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
      Class<? extends Renderer<T_Renderable, T_RenderContext, T_RenderMeta>> renderer);

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
      Renderer<T_Renderable, T_RenderContext, T_RenderMeta> appendix);

  Renderer<?, ?, ?> getDefaultRenderer();

  RendererRepository setDefaultRenderer(Renderer<?, ?, ?> defaultRenderer);

}
