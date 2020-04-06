package net.labyfy.component.gui.mcjfxgl.component.labeled;

import com.google.common.collect.Sets;
import com.sun.javafx.css.converters.PaintConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;

import java.util.Collection;

public class Labeled<T extends Labeled<T>> extends McJfxGLComponent<T> {

  private final StringProperty text = new SimpleStringProperty("");

  private final StyleableObjectProperty<Paint> textFill =
          createStyleableObjectProperty(
                  Labeled.Handle.class,
                  this,
                  "textFill",
                  "-fx-text-fill",
                  PaintConverter.getInstance(),
                  Color.BLACK,
                  Labeled::textFillProperty);

  private final StyleableObjectProperty<Font> textFont =
          createStyleableObjectProperty(
                  Labeled.Handle.class,
                  this,
                  "textFont",
                  "-fx-text-font",
                  PaintConverter.getInstance(),
                  Font.getDefault(),
                  Labeled::textFontProperty);

  protected Labeled() {
  }

  public final StyleableObjectProperty<Font> textFontProperty() {
    return textFont;
  }

  public final Font getTextFont() {
    return textFont.get();
  }

  public final T setTextFont(Font font) {
    textFont.setValue(font);
    return (T) this;
  }

  public final StyleableObjectProperty<Paint> textFillProperty() {
    return textFill;
  }

  public final Paint getTextFill() {
    return textFill.get();
  }

  public final T setTextFill(Paint fill) {
    textFill.setValue(fill);
    return (T) this;
  }

  public final StringProperty textProperty() {
    return this.text;
  }

  public final String getText() {
    return this.text.getValueSafe();
  }

  public final T setText(String text) {
    this.text.setValue(text);
    return (T) this;
  }

  public McJfxGLControl createControl() {
    return new Handle(this);
  }

  @IgnoreInitialization
  public static class Handle extends McJfxGLControl {
    private final Labeled<?> component;

    protected Handle(Labeled<?> component) {
      super(component);
      this.component = component;
    }

    public Collection<CssMetaData<? extends Styleable, ?>> getControlClassMetaData() {
      Collection<CssMetaData<? extends Styleable, ?>> cssMetaData =
              Sets.newHashSet(super.getControlClassMetaData());
      cssMetaData.add(component.textFontProperty().getCssMetaData());
      cssMetaData.add(component.textFillProperty().getCssMetaData());

      for (CssMetaData<? extends Styleable, ?> classCssMetaDatum : Control.getClassCssMetaData()) {
        if (cssMetaData.stream()
                .noneMatch(target -> target.getProperty().equals(classCssMetaDatum.getProperty()))) {
          System.out.println("Missing " + classCssMetaDatum);
        }
      }

      return cssMetaData;
    }
  }
}
