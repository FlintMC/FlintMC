package net.labyfy.component.gui.mcjfxgl.component.style.css;

import javafx.css.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PropertySelfProvidingCssMetaData<S extends Styleable, V> extends CssMetaData<S, V> {

  private final Map<Styleable, StyleableObjectProperty<V>> properties = new HashMap<>();

  protected PropertySelfProvidingCssMetaData(
      String property,
      StyleConverter<?, V> converter,
      V initialValue,
      boolean inherits,
      List<CssMetaData<?, ?>> subProperties) {
    super(property, converter, initialValue, inherits, subProperties);
  }

  protected PropertySelfProvidingCssMetaData(
      String property, StyleConverter<?, V> converter, V initialValue, boolean inherits) {
    super(property, converter, initialValue, inherits);
  }

  protected PropertySelfProvidingCssMetaData(
      String property, StyleConverter<?, V> converter, V initialValue) {
    super(property, converter, initialValue);
  }

  protected PropertySelfProvidingCssMetaData(String property, StyleConverter<?, V> converter) {
    super(property, converter);
  }

  public boolean isSettable(S styleable) {
    this.ensureExistence(styleable);
    return !this.properties.get(styleable).isBound();
  }

  public StyleableProperty<V> getStyleableProperty(S styleable) {
    this.ensureExistence(styleable);
    return this.properties.get(styleable);
  }

  private void ensureExistence(S styleable) {
    if (!this.properties.containsKey(styleable)) {
      this.properties.putIfAbsent(styleable, this.createProperty(styleable));
    }
  }

  protected abstract StyleableObjectProperty<V> createProperty(S styleable);
}
