package net.labyfy.component.gui.mcjfxgl.component;

import com.sun.javafx.css.converters.SizeConverter;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import net.labyfy.component.gui.old.adapter.GuiAdapter;
import net.labyfy.component.gui.old.component.GuiComponent;
import net.labyfy.component.gui.mcjfxgl.component.property.PropertyBuilder;
import net.labyfy.component.gui.mcjfxgl.component.property.convert.BackgroundConverter;

public abstract class McJfxGLComponent<T extends McJfxGLComponent<T>> {

  private McJfxGLControl control;

  public static final CssMetaData<McJfxGLControl, Number> WIDTH_META =
          PropertyBuilder.<McJfxGLComponent<?>, Number>create()
                  .setName("width")
                  .setProperty("-fx-width")
                  .setParent(McJfxGLControl.class)
                  .setInitialValue(100d)
                  .setStyleable(true)
                  .setPropertySupplier(McJfxGLComponent::widthProperty)
                  .setStyleablePropertyCreator(
                          (propertyBuilder, metaData) -> new SimpleStyleableDoubleProperty(metaData))
                  .setStyleConverter(SizeConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, Number> HEIGHT_META =
          PropertyBuilder.<McJfxGLComponent<?>, Number>create()
                  .setName("height")
                  .setProperty("-fx-height")
                  .setParent(McJfxGLControl.class)
                  .setInitialValue(30d)
                  .setStyleable(true)
                  .setPropertySupplier(McJfxGLComponent::heightProperty)
                  .setStyleablePropertyCreator(
                          (propertyBuilder, metaData) -> new SimpleStyleableDoubleProperty(metaData))
                  .setStyleConverter(SizeConverter.getInstance())
                  .buildMeta();


  public static final CssMetaData<McJfxGLControl, Background> BACKGROUND_META =
          PropertyBuilder.<McJfxGLComponent<?>, Background>create()
                  .setName("background")
                  .setProperty("-fx-region-background")
                  .setParent(McJfxGLControl.class)
                  .setInitialValue(null)
                  .setStyleable(true)
                  .setStyleConverter(BackgroundConverter.getInstance())
                  .setPropertySupplier((McJfxGLComponent::backgroundProperty))
                  .setSubProperties(Background.getClassCssMetaData())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, Number> TRANSLATE_X_META =
          PropertyBuilder.<McJfxGLComponent<?>, Number>create()
                  .setName("translate-x")
                  .setProperty("-fx-translate.x")
                  .setParent(McJfxGLControl.class)
                  .setInitialValue(0d)
                  .setStyleable(true)
                  .setPropertySupplier(McJfxGLComponent::translateXProperty)
                  .setStyleablePropertyCreator(
                          (propertyBuilder, metaData) -> new SimpleStyleableDoubleProperty(metaData))
                  .setStyleConverter(SizeConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, Number> TRANSLATE_Y_META =
          PropertyBuilder.<McJfxGLComponent<?>, Number>create()
                  .setName("translate-y")
                  .setProperty("-fx-translate.y")
                  .setParent(McJfxGLControl.class)
                  .setInitialValue(0d)
                  .setStyleable(true)
                  .setPropertySupplier(McJfxGLComponent::translateYProperty)
                  .setStyleablePropertyCreator(
                          (propertyBuilder, metaData) -> new SimpleStyleableDoubleProperty(metaData))
                  .setStyleConverter(SizeConverter.getInstance())
                  .buildMeta();

  private final StyleableDoubleProperty width = new SimpleStyleableDoubleProperty(WIDTH_META);

  private final StyleableDoubleProperty height = new SimpleStyleableDoubleProperty(HEIGHT_META);

  private final SimpleStyleableObjectProperty<Background> background =
          new SimpleStyleableObjectProperty<>(BACKGROUND_META);

  private final SimpleStyleableDoubleProperty translateX = new SimpleStyleableDoubleProperty(TRANSLATE_X_META);

  private final SimpleStyleableDoubleProperty translateY = new SimpleStyleableDoubleProperty(TRANSLATE_Y_META);


  public StyleableDoubleProperty translateXProperty() {
    return translateX;
  }

  public StyleableDoubleProperty translateYProperty() {
    return translateY;
  }

  public final double getTranslateX() {
    return translateX.getValue();
  }

  public final double getTranslateY() {
    return translateY.getValue();
  }

  public final T setTranslateY(double translateY) {
    this.translateY.set(translateY);
    return (T) this;
  }

  public final T setTranslateX(double translateX) {
    this.translateX.set(translateX);
    return (T) this;
  }

  public final SimpleStyleableObjectProperty<Background> backgroundProperty() {
    return background;
  }

  public final Background getBackground() {
    return this.background.getValue();
  }

  public final T setBackground(Background background) {
    this.background.setValue(background);
    return (T) this;
  }

  public final StyleableDoubleProperty heightProperty() {
    return this.height;
  }

  public final double getHeight() {
    return height.getValue();
  }

  public final T setHeight(double height) {
    this.height.setValue(height);
    return (T) this;
  }

  public final StyleableDoubleProperty widthProperty() {
    return this.width;
  }

  public final double getWidth() {
    return this.width.getValue();
  }

  public final T setWidth(double width) {
    this.width.setValue(width);
    return (T) this;
  }

  public abstract McJfxGLControl createControl();

  public McJfxGLControl getControl() {
    if (this.control == null) this.control = this.createControl();
    return control;
  }

}
