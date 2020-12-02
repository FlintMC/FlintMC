package net.flintmc.render.model;

import net.flintmc.util.property.PropertyContextAware;

import java.util.function.Consumer;

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

  /**
   * Calls the handler that is used only to set properties.
   *
   * @return this
   */
  T_Renderable callPropertyHandler();

  T_Renderable callPropertyPreparations();

  T_Renderable callRenderCleanup();

  /**
   * Adds a pre-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callRenderPreparations()} ()}. Should not be used to set any properties before the
   * render. For this @see {@link Renderable#setPropertyHandler(Consumer)}.
   *
   * @param consumer the hook to add
   * @return this
   */
  T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer);

  T_Renderable addPropertyPreparation(Consumer<T_Renderable> consumer);

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
