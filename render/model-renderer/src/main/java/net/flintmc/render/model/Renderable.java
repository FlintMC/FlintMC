package net.flintmc.render.model;

import java.util.function.Consumer;

/**
 * Defines an abstract component that can be rendered.
 *
 * @param <T_RenderContextAware> the component that is the "owner" of the provided render context.
 * @param <T_RenderContext>      the component that coordinates the rendering process.
 * @param <T_Renderable>         the type of this component. Just used here to lock the generic types of the
 *                               system.
 * @param <T_RenderMeta>         the type of metadata that can be attached to the component.
 */
public interface Renderable<
    T_RenderContextAware extends RenderContextAware<T_RenderContext>,
    T_RenderContext extends
        RenderContext<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
    T_Renderable extends
        Renderable<T_RenderContextAware, T_RenderContext, T_Renderable, T_RenderMeta>,
    T_RenderMeta> {

  /**
   * @return the metadata attached to this renderable component
   */
  T_RenderMeta getMeta();

  /**
   * @return the component that coordinates the rendering process
   */
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
   * @return calls the handler that is used only to set properties.
   */
  T_Renderable callPropertyHandler();

  T_Renderable addRenderPreparation(Consumer<T_Renderable> consumer);

  T_Renderable setPropertyHandler(Consumer<T_Renderable> consumer);

  <T_PropertyType, T_Property_Meta> T_Renderable setPropertyValue(
      Property<T_PropertyType, T_Property_Meta> property, T_PropertyType propertyValue);

  <T_PropertyType, T_Property_Meta> T_Renderable setPropertyMeta(
      Property<T_PropertyType, T_Property_Meta> property, T_Property_Meta propertyMode);

  <T_PropertyType, T_PropertyMeta> T_PropertyType getPropertyValue(
      Property<T_PropertyType, T_PropertyMeta> property);

  <T_PropertyType, T_PropertyMeta> T_PropertyMeta getPropertyMeta(
      Property<T_PropertyType, T_PropertyMeta> property);

  interface Property<T_PropertyValue, T_PropertyMeta> {
    T_PropertyValue getDefaultValue();

    T_PropertyMeta getDefaultMeta();
  }

  interface FloatProperty<T_RenderMeta> extends Property<Float, T_RenderMeta> {
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
