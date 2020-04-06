package net.labyfy.component.gui.mcjfxgl.component.control;

import com.sun.javafx.css.converters.SizeConverter;
import javafx.beans.property.*;
import javafx.css.*;
import javafx.scene.control.Control;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@IgnoreInitialization
public class McJfxGLControl extends McJfxGLControlBase {
  private static final Map<String, CssMetaData> META_DATA = new HashMap<>();

  protected McJfxGLControl(McJfxGLComponent component) {
    super(component);
  }

  public static DoubleProperty createDoubleProperty(
          Control control, String propertyName, double initialValue) {
    return new SimpleDoubleProperty(control, propertyName, initialValue);
  }

  public static <T extends McJfxGLControlBase> ObjectProperty<T> createObjectProperty(
          Control control, String propertyName, T initialValue) {
    return new SimpleObjectProperty<>(control, propertyName, initialValue);
  }

  public static <T extends McJfxGLControlBase>
  StyleableDoubleProperty createStyleableDoubleProperty(
          T control,
          String propertyName,
          String property,
          double initialValue,
          Function<T, Property<? extends Number>> propertySupplier) {

    CssMetaData<T, Number> metaData =
            createOrGetMetaData(property, SizeConverter.getInstance(), initialValue, propertySupplier);

    return new SimpleStyleableDoubleProperty(metaData, control, propertyName, initialValue);
  }

  public static <T extends McJfxGLControlBase, K>
  StyleableObjectProperty<K> createStyleableObjectProperty(
          T control,
          String propertyName,
          String property,
          StyleConverter<?, ?> converter,
          K initialValue,
          Function<T, Property<? extends K>> propertySupplier) {

    CssMetaData<T, K> metaData =
            McJfxGLControl.createOrGetMetaData(
                    property, converter, initialValue, propertySupplier);

    return new SimpleStyleableObjectProperty<>(metaData, control, propertyName, initialValue);
  }

  public static <T extends Styleable, K> CssMetaData<T, K> getMetaData(String property) {
    return META_DATA.get(property);
  }

  public static <T extends McJfxGLControlBase, K> CssMetaData<T, K> createOrGetMetaData(
          String property,
          StyleConverter<?, ?> converter,
          K initialValue,
          Function<T, Property<? extends K>> propertySupplier) {

    if (!META_DATA.containsKey(property)) {
      META_DATA.put(
              property,
              new CssMetaData<T, Object>(
                      property, (StyleConverter<?, Object>) converter, initialValue, true) {
                public boolean isSettable(T styleable) {
                  return !propertySupplier.apply(styleable).isBound();
                }

                public StyleableProperty<Object> getStyleableProperty(T styleable) {
                  return (StyleableProperty<Object>) propertySupplier.apply(styleable);
                }
              });
    } else {
      return META_DATA.get(property);
    }
    return META_DATA.get(property);
  }

}
