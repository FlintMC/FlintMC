package net.labyfy.component.gui.mcjfxgl.component.style.css;

import javafx.css.*;

import java.util.HashMap;

public class StyleableObjectPropertySelfProvidingCssMetaData<S extends Styleable, V>
        extends PropertySelfProvidingCssMetaData<S, V> {

  public StyleableObjectPropertySelfProvidingCssMetaData(
          String property, V initialValue, StyleConverter<?, V> styleConverter, boolean inherits) {
    super(property, styleConverter, initialValue, inherits);
  }

  protected StyleableObjectProperty<V> createProperty(S styleable) {
    return new SimpleStyleableObjectProperty<>(
            this, styleable, getProperty(), getInitialValue(styleable));
  }
}
