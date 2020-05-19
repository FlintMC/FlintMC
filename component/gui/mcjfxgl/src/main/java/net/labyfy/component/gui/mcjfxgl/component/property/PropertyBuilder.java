package net.labyfy.component.gui.mcjfxgl.component.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PropertyBuilder<M extends McJfxGLComponent<?>, T> {

  private static final Map<Class<?>, Map<String, CssMetaData<?, ?>>> META_DATA = new HashMap<>();

  private boolean styleable = true;
  private Class<?> parent;
  private String name;
  private String property;
  private StyleConverter<?, ?> styleConverter;
  private List<CssMetaData<? extends Styleable, ?>> subProperties = Lists.newArrayList();
  private Function<M, Property<T>> propertySupplier;
  private T initialValue = null;
  private BiFunction<PropertyBuilder<M, T>, CssMetaData<McJfxGLControl, T>, ? extends Property<T>>
          styleablePropertyCreator =
          (propertyBuilder, metaData) -> new SimpleStyleableObjectProperty<>(metaData);
  private Function<PropertyBuilder<M, T>, ? extends Property<T>> propertyCreator =
          propertyBuilder ->
                  new SimpleObjectProperty<>(null, propertyBuilder.name, propertyBuilder.initialValue);

  private PropertyBuilder() {
  }

  private static <M extends McJfxGLComponent<?>, T>
  CssMetaData<McJfxGLControl, T> createOrGetMetaData(
          Class<?> parent,
          String property,
          StyleConverter<?, ?> converter,
          T initialValue,
          Function<M, Property<T>> propertySupplier,
          List<CssMetaData<? extends Styleable, ?>> children) {

    if (!META_DATA.containsKey(parent)) {
      META_DATA.put(parent, Maps.newHashMap());
    }
    Map<String, CssMetaData<?, ?>> metaData = META_DATA.get(parent);

    if (!metaData.containsKey(property)) {
      metaData.put(
              property,
              new CssMetaData<McJfxGLControl, T>(
                      property, (StyleConverter<?, T>) converter, initialValue, true, children) {
                public boolean isSettable(McJfxGLControl styleable) {
                  return !propertySupplier.apply((M) styleable.getComponent()).isBound();
                }

                public StyleableProperty<T> getStyleableProperty(McJfxGLControl styleable) {
                  return (StyleableProperty<T>) propertySupplier.apply((M) styleable.getComponent());
                }
              });
    }
    return (CssMetaData<McJfxGLControl, T>) metaData.get(property);
  }

  public static <M extends McJfxGLComponent<?>, T> PropertyBuilder<M, T> create() {
    return new PropertyBuilder<M, T>();
  }

  public CssMetaData<McJfxGLControl, T> buildMeta() {
    return PropertyBuilder.createOrGetMetaData(
            this.parent,
            this.property,
            this.styleConverter,
            this.initialValue,
            this.propertySupplier,
            this.subProperties);
  }

  public <K> K build() {
    if (styleable) return (K) this.styleablePropertyCreator.apply(this, this.buildMeta());
    return (K) this.propertyCreator.apply(this);
  }

  public PropertyBuilder<M, T> setStyleablePropertyCreator(
          BiFunction<PropertyBuilder<M, T>, CssMetaData<McJfxGLControl, T>, ? extends Property<T>>
                  styleablePropertyCreator) {
    this.styleablePropertyCreator = styleablePropertyCreator;
    return this;
  }

  public PropertyBuilder<M, T> setPropertySupplier(Function<M, Property<T>> propertySupplier) {
    this.propertySupplier = propertySupplier;
    return this;
  }

  public PropertyBuilder<M, T> setStyleable(boolean styleable) {
    this.styleable = styleable;
    return this;
  }

  public PropertyBuilder<M, T> setName(String name) {
    Preconditions.checkNotNull(name);
    this.name = name;
    return this;
  }

  public PropertyBuilder<M, T> setProperty(String property) {
    Preconditions.checkNotNull(property);
    this.property = property;
    return this;
  }

  public PropertyBuilder<M, T> setPropertyCreator(
          Function<PropertyBuilder<M, T>, ? extends Property<T>> propertyCreator) {
    this.propertyCreator = propertyCreator;
    return this;
  }

  public PropertyBuilder<M, T> setSubProperties(
          List<CssMetaData<?, ?>> subProperties) {
    this.subProperties = Lists.newArrayList(subProperties);
    return this;
  }

  public PropertyBuilder<M, T> setParent(Class<?> parent) {
    this.parent = parent;
    return this;
  }

  public PropertyBuilder<M, T> setStyleConverter(StyleConverter<?, ?> styleConverter) {
    this.styleConverter = styleConverter;
    return this;
  }

  public PropertyBuilder<M, T> setInitialValue(T initialValue) {
    this.initialValue = initialValue;
    return this;
  }
}
