package net.labyfy.component.gui.mcjfxgl.component.property.convert;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.Styleable;
import javafx.scene.layout.*;

import java.lang.reflect.Field;
import java.util.Map;

public class BackgroundConverter extends StyleConverterImpl<ParsedValue[], Background> {

  private StyleConverterImpl<ParsedValue[], Background> converter;

  private static class Lazy {
    private static BackgroundConverter INSTANCE = new BackgroundConverter();
  }

  private BackgroundConverter() {
    try {
      Class<?> clazz = Class.forName("javafx.scene.layout.BackgroundConverter");
      Field instance = clazz.getDeclaredField("INSTANCE");
      instance.setAccessible(true);

      this.converter = (StyleConverterImpl<ParsedValue[], Background>) instance.get(null);

    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static BackgroundConverter getInstance() {
    return Lazy.INSTANCE;
  }

  public Background convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
    return this.converter.convert(convertedValues);
  }
}
