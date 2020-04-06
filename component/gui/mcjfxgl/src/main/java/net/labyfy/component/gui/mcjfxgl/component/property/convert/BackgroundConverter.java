package net.labyfy.component.gui.mcjfxgl.component.property.convert;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.layout.Background;
import javafx.scene.layout.LegacyBackgroundConverterAccessor;

public class BackgroundConverter {

  private BackgroundConverter() {
  }

  public static StyleConverter<ParsedValue[], Background> getInstance() {
    return LegacyBackgroundConverterAccessor.get();
  }
}
