package net.flintmc.render.model;

import java.util.function.Consumer;

/**
 * Defines an abstract component that can be rendered.
 *
 * @param <T_RenderContextAware> the component that is the "owner" of the provided render context.
 * @param <T_RenderContext>      the component that coordinates the rendering process.
 * @param <T_Renderable>         the type of this component. Just used here to lock the generic types of the
 *                               system.
 * @param <T_RenderTarget>       the type of metadata that can be attached to the component.
 */
public interface Renderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, ?, T_RenderTarget>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderTarget>,
    T_RenderTarget> {

  /** @return the metadata attached to this renderable component */
  T_RenderTarget getMeta();

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

  /**
   * Adds a pre-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callRenderPreparations()} ()}. Should not be used to set any properties before the
   * render. For this @see {@link Renderable#setPropertyHandler(Consumer)}.
   *
   * @param consumer the hook to add
   * @return this
   */
  T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer);

  /**
   * Sets the pre-render hook that can be called - usually by the {@link Renderer} - with {@link
   * Renderable#callPropertyHandler()}. Should not be used to set anything else than properties
   * before the render. For this @see {@link Renderable#addRenderPreparation(Consumer)} (Consumer)}.
   *
   * @param consumer the new property handler
   * @return this
   */
  T_Renderable setPropertyHandler(Consumer<T_Renderable> consumer);

  /**
   * @param property the property to modify on this instance
   * @param propertyValue the value to set the property to
   * @param <T_PropertyType> @see {@link Property<T_PropertyType>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return this
   */
  <T_PropertyType, T_PropertyMeta> T_Renderable setPropertyValue(
      Property<T_PropertyType, T_PropertyMeta> property, T_PropertyType propertyValue);

  /**
   * @param property the property to modify on this instance
   * @param propertyMode the mode to set the property to
   * @param <T_PropertyType> @see {@link Property<T_PropertyType>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return this
   */
  <T_PropertyType, T_PropertyMeta> T_Renderable setPropertyMeta(
      Property<T_PropertyType, T_PropertyMeta> property, T_PropertyMeta propertyMode);

  /**
   * @param property the property to get the value from
   * @param <T_PropertyType> @see {@link Property<T_PropertyType>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return the current value of the given property
   */
  <T_PropertyType, T_PropertyMeta> T_PropertyType getPropertyValue(
      Property<T_PropertyType, T_PropertyMeta> property);

  /**
   * @param property the property to get the mode from
   * @param <T_PropertyType> @see {@link Property<T_PropertyType>}
   * @param <T_PropertyMeta> @see {@link Property<T_PropertyMeta>}
   * @return the current mode of the given property
   */
  <T_PropertyType, T_PropertyMeta> T_PropertyMeta getPropertyMeta(
      Property<T_PropertyType, T_PropertyMeta> property);

  interface Property<T_PropertyValue, T_PropertyMeta> {
    boolean validateValue(T_PropertyValue propertyValue);

    boolean validateMeta(T_PropertyMeta propertyMeta);

    T_PropertyValue getDefaultValue();

    T_PropertyMeta getDefaultMeta();
  }

  interface StringProperty<T_PropertyMeta> extends Property<String, T_PropertyMeta> {
    default String getDefaultValue() {
      return null;
    }
  }

  interface IntProperty<T_PropertyMeta> extends Property<Integer, T_PropertyMeta> {
    default Integer getDefaultValue() {
      return 0;
    }
  }

  interface FloatProperty<T_PropertyMeta> extends Property<Float, T_PropertyMeta> {
    default Float getDefaultValue() {
      return 0f;
    }
  }

  interface BooleanProperty extends Property<Boolean, Void> {
    default Boolean getDefaultValue() {
      return false;
    }
  }
}
