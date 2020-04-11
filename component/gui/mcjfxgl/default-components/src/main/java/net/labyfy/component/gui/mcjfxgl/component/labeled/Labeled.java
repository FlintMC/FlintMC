package net.labyfy.component.gui.mcjfxgl.component.labeled;

import com.google.common.collect.Sets;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.PaintConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.property.PropertyBuilder;

import java.util.Arrays;
import java.util.Collection;

public class Labeled<T extends Labeled<T>> extends McJfxGLComponent<T> {

  private final StringProperty text = new SimpleStringProperty("");

  public static CssMetaData<McJfxGLControl, Paint> TEXT_FILL_META;

  public static CssMetaData<McJfxGLControl, Font> TEXT_FONT_META;

  private final StyleableObjectProperty<Font> textFont;

  private final StyleableObjectProperty<Paint> textFill;


  protected Labeled() {
    if (TEXT_FILL_META == null)
      TEXT_FILL_META =
              PropertyBuilder.<Labeled<?>, Paint>create()
                      .setName("textFill")
                      .setProperty("-fx-text-fill")
                      .setParent(Labeled.Handle.class)
                      .setStyleConverter(PaintConverter.getInstance())
                      .setPropertySupplier(Labeled::textFillProperty)
                      .setInitialValue(Color.BLACK)
                      .buildMeta();

    if (TEXT_FONT_META == null)
      TEXT_FONT_META =
              PropertyBuilder.<Labeled<?>, Font>create()
                      .setName("textFont")
                      .setProperty("-fx-text-font")
                      .setParent(Labeled.Handle.class)
                      .setStyleConverter(FontConverter.getInstance())
                      .setPropertySupplier(Labeled::textFontProperty)
                      .setInitialValue(Font.getDefault())
                      .buildMeta();

    this.textFont = new SimpleStyleableObjectProperty<>(TEXT_FONT_META);

    this.textFill = new SimpleStyleableObjectProperty<>(TEXT_FILL_META);

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
      cssMetaData.addAll(Arrays.asList(TEXT_FILL_META, TEXT_FONT_META));

      return cssMetaData;
    }
  }
}
