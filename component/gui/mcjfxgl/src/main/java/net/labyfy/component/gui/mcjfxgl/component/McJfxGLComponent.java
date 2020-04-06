package net.labyfy.component.gui.mcjfxgl.component;

import com.google.common.collect.Maps;
import com.sun.javafx.css.converters.SizeConverter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.css.*;
import javafx.scene.control.Control;
import javafx.scene.layout.Background;
import net.labyfy.component.gui.mcjfxgl.component.property.convert.BackgroundConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class McJfxGLComponent<T extends McJfxGLComponent<T>> {

  private static final Map<Class<? extends Control>, Map<String, CssMetaData<?, ?>>> META_DATA =
          new HashMap<>();
  private McJfxGLControl control;

  private final DoubleProperty width =
          createStyleableDoubleMinPrefMaxProperty(
                  McJfxGLControl.class,
                  this,
                  "maxWidth",
                  "-fx-width",
                  100,
                  McJfxGLComponent::widthProperty);

  private final DoubleProperty height =
          createStyleableDoubleMinPrefMaxProperty(
                  McJfxGLControl.class,
                  this,
                  "maxWidth",
                  "-fx-height",
                  30,
                  McJfxGLComponent::heightProperty);

  private final ObjectProperty<Background> background =
          createStyleableObjectProperty(
                  McJfxGLControl.class,
                  this,
                  "background",
                  "-fx-region-background",
                  BackgroundConverter.getInstance(),
                  null,
                  McJfxGLComponent::backgroundProperty);

  public static <T extends McJfxGLComponent> DoubleProperty createStyleableDoubleMinPrefMaxProperty(
          Class<? extends Control> parentClass,
          T control,
          String propertyName,
          String property,
          double initialValue,
          Function<T, Property<? extends Number>> propertySupplier) {
    CssMetaData<? extends Control, Number> metaData =
            createOrGetMetaData(
                    parentClass, property, SizeConverter.getInstance(), initialValue, propertySupplier);

    return new SimpleStyleableDoubleProperty(metaData, control, propertyName, initialValue) {
      protected void invalidated() {
        control.getControl().requestLayout();
      }
    };
  }

  public static <T extends McJfxGLComponent> StyleableDoubleProperty createStyleableDoubleProperty(
          Class<? extends McJfxGLControl> parentClass,
          T control,
          String propertyName,
          String property,
          double initialValue,
          Function<T, Property<? extends Number>> propertySupplier) {

    CssMetaData<? extends Control, Number> metaData =
            createOrGetMetaData(
                    parentClass, property, SizeConverter.getInstance(), initialValue, propertySupplier);

    return new SimpleStyleableDoubleProperty(metaData, control, propertyName, initialValue);
  }

  public static <T extends McJfxGLComponent, K>
  StyleableObjectProperty<K> createStyleableObjectProperty(
          Class<? extends McJfxGLControl> parentClass,
          T control,
          String propertyName,
          String property,
          StyleConverter<?, ?> converter,
          K initialValue,
          Function<T, Property<? extends K>> propertySupplier) {

    CssMetaData<? extends McJfxGLControl, K> metaData =
            createOrGetMetaData(parentClass, property, converter, initialValue, propertySupplier);

    return new SimpleStyleableObjectProperty<>(metaData, control, propertyName, initialValue);
  }

  public static <T extends McJfxGLComponent, K>
  CssMetaData<? extends McJfxGLControl, K> createOrGetMetaData(
          Class<? extends Control> parentClass,
          String property,
          StyleConverter<?, ?> converter,
          K initialValue,
          Function<T, Property<? extends K>> propertySupplier) {

    if (!META_DATA.containsKey(parentClass)) {
      META_DATA.put(parentClass, Maps.newHashMap());
    }
    Map<String, CssMetaData<?, ?>> metaData = META_DATA.get(parentClass);

    if (!metaData.containsKey(property)) {
      metaData.put(
              property,
              new CssMetaData<McJfxGLControl, Object>(
                      property, (StyleConverter<?, Object>) converter, initialValue, true) {
                public boolean isSettable(McJfxGLControl styleable) {
                  return !propertySupplier.apply((T) styleable.getComponent()).isBound();
                }

                public StyleableProperty<Object> getStyleableProperty(McJfxGLControl styleable) {
                  return (StyleableProperty<Object>)
                          propertySupplier.apply((T) styleable.getComponent());
                }
              });
    } else {
      return (CssMetaData<? extends McJfxGLControl, K>) metaData.get(property);
    }
    return (CssMetaData<? extends McJfxGLControl, K>) metaData.get(property);
  }

  public final ObjectProperty<Background> backgroundProperty() {
    return background;
  }

  public final Background getBackground() {
    return this.background.getValue();
  }

  public final T setBackground(Background background) {
    this.background.setValue(background);
    return (T) this;
  }

  public final DoubleProperty heightProperty() {
    return this.height;
  }

  public final double getHeight() {
    return height.getValue();
  }

  public abstract McJfxGLControl createControl();

  public final McJfxGLControl getControl() {
    if (this.control == null) this.control = this.createControl();
    return this.control;
  }

  public final T setHeight(double height) {
    this.height.setValue(height);
    return (T) this;
  }

  public final DoubleProperty widthProperty() {
    return this.width;
  }

  public final double getWidth() {
    return this.width.getValue();
  }

  public final T setWidth(double width) {
    this.width.setValue(width);
    return (T) this;
  }
}
