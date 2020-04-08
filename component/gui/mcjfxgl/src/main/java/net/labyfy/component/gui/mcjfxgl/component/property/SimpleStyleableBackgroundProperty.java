package net.labyfy.component.gui.mcjfxgl.component.property;

import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.*;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleableObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.property.convert.BackgroundConverter;

import java.util.Arrays;
import java.util.List;

public class SimpleStyleableBackgroundProperty extends SimpleStyleableObjectProperty<Background> {

  public static final CssMetaData<McJfxGLControl, Paint> COLOR_META =
          PropertyBuilder.<McJfxGLComponent<?>, Paint>create()
                  .setName("background-color")
                  .setProperty("-fx-background-color")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().colorProperty())
                  .setInitialValue(Color.WHITE)
                  .setStyleConverter(PaintConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, Image[]> IMAGE_META =
          PropertyBuilder.<McJfxGLComponent<?>, Image[]>create()
                  .setName("background-image")
                  .setProperty("-fx-background-image")
                  .setStyleConverter(URLConverter.SequenceConverter.getInstance())
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().imageProperty())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, Insets[]> INSETS_META =
          PropertyBuilder.<McJfxGLComponent<?>, Insets[]>create()
                  .setName("background-insets")
                  .setProperty("-fx-background-insets")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().insetsProperty())
                  .setStyleConverter(BackgroundConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, CornerRadii[]> RADIUS_META =
          PropertyBuilder.<McJfxGLComponent<?>, CornerRadii[]>create()
                  .setName("background-radius")
                  .setProperty("-fx-background-radius")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().radiusProperty())
                  .setStyleConverter(CornerRadiiConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, RepeatStruct[]> REPEAT_META =
          PropertyBuilder.<McJfxGLComponent<?>, RepeatStruct[]>create()
                  .setName("background-repeat")
                  .setProperty("-fx-background-repeat")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().repeatProperty())
                  .setStyleConverter(RepeatStructConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, BackgroundPosition[]> POSITION_META =
          PropertyBuilder.<McJfxGLComponent<?>, BackgroundPosition[]>create()
                  .setName("background-position")
                  .setProperty("-fx-background-position")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().positionProperty())
                  .setStyleConverter(BackgroundPositionConverter.getInstance())
                  .buildMeta();

  public static final CssMetaData<McJfxGLControl, BackgroundSize[]> SIZE_META =
          PropertyBuilder.<McJfxGLComponent<?>, BackgroundSize[]>create()
                  .setName("background-size")
                  .setProperty("-fx-background-size")
                  .setPropertySupplier(
                          mcJfxGLComponent -> mcJfxGLComponent.backgroundProperty().sizeProperty())
                  .setStyleConverter(BackgroundSizeConverter.getInstance())
                  .buildMeta();

  private final StyleableObjectProperty<Paint> color =
          new SimpleStyleableObjectProperty<>(COLOR_META);

  private final StyleableObjectProperty<Image[]> image =
          new SimpleStyleableObjectProperty<>(IMAGE_META);

  private final StyleableObjectProperty<Insets[]> insets =
          new SimpleStyleableObjectProperty<>(INSETS_META);

  private final StyleableObjectProperty<CornerRadii[]> radius =
          new SimpleStyleableObjectProperty<>(RADIUS_META);

  private final StyleableObjectProperty<RepeatStruct[]> repeat =
          new SimpleStyleableObjectProperty<>(REPEAT_META);

  private final StyleableObjectProperty<BackgroundPosition[]> position =
          new SimpleStyleableObjectProperty<>(POSITION_META);

  private final StyleableObjectProperty<BackgroundSize[]> size =
          new SimpleStyleableObjectProperty<>(SIZE_META);

  public SimpleStyleableBackgroundProperty(CssMetaData<? extends McJfxGLControl, Background> meta) {
    super(meta);
  }

  public static List<CssMetaData<McJfxGLControl, ?>> getMeta() {
    return Arrays.asList(
            COLOR_META, IMAGE_META, INSETS_META, RADIUS_META, REPEAT_META, POSITION_META, SIZE_META);
  }

  public final StyleableObjectProperty<Paint> colorProperty() {
    return this.color;
  }

  public final StyleableObjectProperty<Image[]> imageProperty() {
    return this.image;
  }

  public final StyleableObjectProperty<Insets[]> insetsProperty() {
    return insets;
  }

  public final StyleableObjectProperty<CornerRadii[]> radiusProperty() {
    return radius;
  }

  public final StyleableObjectProperty<RepeatStruct[]> repeatProperty() {
    return repeat;
  }

  public final StyleableObjectProperty<BackgroundPosition[]> positionProperty() {
    return position;
  }

  public final StyleableObjectProperty<BackgroundSize[]> sizeProperty() {
    return size;
  }
}
