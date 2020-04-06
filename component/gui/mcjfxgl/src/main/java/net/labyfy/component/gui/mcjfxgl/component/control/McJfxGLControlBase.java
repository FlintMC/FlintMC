package net.labyfy.component.gui.mcjfxgl.component.control;

import com.google.common.collect.ImmutableMap;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.StringConverter;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Duration;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.style.css.StyleableObjectPropertySelfProvidingCssMetaData;
import net.labyfy.component.gui.mcjfxgl.component.style.css.animate.PropertyAnimationTimer;
import net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate.PropertyInterpolator;
import net.labyfy.component.gui.mcjfxgl.component.theme.ThemeRepository;
import net.labyfy.component.inject.InjectionHolder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@IgnoreInitialization
public class McJfxGLControlBase extends Control implements GuiComponent {

  private static final Map<
          Class<? extends McJfxGLControlBase>, Collection<CssMetaData<? extends Styleable, ?>>>
          cssMetaData = new HashMap<>();

  private static final Map<
          Class<? extends McJfxGLControlBase>, Collection<CssMetaData<? extends Styleable, ?>>>
          modifiedMetaData = new HashMap<>();

  private static final Map<String, String> MODIFY_PROPERTIES =
          new HashMap<>(
                  ImmutableMap.<String, String>builder()
                          .put("-fx-region-border", "-fx-border")
                          .put("-fx-region-background", "-fx-background")
                          .build());

  private final Collection<PropertyAnimationTimer> propertyAnimationTimers = new HashSet<>();
  private final McJfxGLComponent component;
  private Class<?> defaultSkinClass;

  protected McJfxGLControlBase(McJfxGLComponent component) {
    this.component = component;
    modifiedMetaData.putIfAbsent(getClass(), new CopyOnWriteArraySet<>());
    cssMetaData.putIfAbsent(getClass(), new CopyOnWriteArraySet<>());
    cssMetaData.get(getClass()).addAll(getControlClassMetaData());

    for (CssMetaData<? extends Styleable, ?> metaData : cssMetaData.get(getClass())) {
      if (!MODIFY_PROPERTIES.containsKey(metaData.getProperty())) {
        MODIFY_PROPERTIES.put(metaData.getProperty(), metaData.getProperty());
      }
      if (modifiedMetaData.get(getClass()).contains(metaData)) continue;
      addTransitionProperties(null, metaData, "transition-duration", "transition-interpolator");
      modifiedMetaData.get(getClass()).add(metaData);
    }
  }

  private static Collection<CssMetaData<? extends Styleable, ?>> getRecursiveMetaData(
      CssMetaData<? extends Styleable, ?> metaData) {
    if (metaData.getSubProperties() == null) return Collections.singletonList(metaData);
    Collection<CssMetaData<? extends Styleable, ?>> children =
        new HashSet<>(metaData.getSubProperties());
    for (CssMetaData<? extends Styleable, ?> child : new ArrayList<>(children)) {
      children.addAll(getRecursiveMetaData(child));
    }
    children.add(metaData);
    return children;
  }

  private void addTransitionProperties(
      CssMetaData parent, CssMetaData metaData, String translationName, String interpolationName) {
    try {

      if (MODIFY_PROPERTIES.get(metaData.getProperty()).endsWith(translationName)) return;

      Field initialValue = CssMetaData.class.getDeclaredField("initialValue");
      initialValue.setAccessible(true);

      CssMetaData transitionDummy =
          new StyleableObjectPropertySelfProvidingCssMetaData<>(
              MODIFY_PROPERTIES.get(metaData.getProperty()) + "-transition-duration",
              initialValue.get(metaData),
              metaData.getConverter(),
              metaData.isInherits());

      CssMetaData transitionDuration =
              new StyleableObjectPropertySelfProvidingCssMetaData<>(
                      MODIFY_PROPERTIES.get(metaData.getProperty()) + "-transition-duration",
                      Duration.ZERO,
                      DurationConverter.getInstance(),
                      true);

      SimpleObjectProperty<Interpolator> interpolator =
          new SimpleObjectProperty<>(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

      CssMetaData transitionInterpolator =
          new StyleableObjectPropertySelfProvidingCssMetaData(
              MODIFY_PROPERTIES.get(metaData.getProperty()) + "-transition-interpolator",
              Interpolator.SPLINE(0.25, 0.1, 0.25, 1),
              StringConverter.getInstance(),
              true) {
            @Override
            protected StyleableObjectProperty createProperty(Styleable styleable) {
              StyleableObjectProperty<Object> interpolatorProxy = super.createProperty(styleable);
              interpolatorProxy.addListener(
                  (observable, oldValue, newValue) -> {
                    if (newValue instanceof Number[] && ((Number[]) newValue).length == 4) {
                      Number[] numbers = ((Number[]) newValue);
                      interpolator.setValue(
                          Interpolator.SPLINE(
                              numbers[0].doubleValue(),
                              numbers[1].doubleValue(),
                              numbers[2].doubleValue(),
                              numbers[3].doubleValue()));
                    } else if (newValue instanceof String) {
                      Interpolator newInterpolator = interpolator.get();
                      switch (((String) newValue).toLowerCase()) {
                        case "ease-in":
                          newInterpolator = Interpolator.EASE_IN;
                          break;
                        case "ease-out":
                          newInterpolator = Interpolator.EASE_OUT;
                          break;
                        case "ease-both":
                          newInterpolator = Interpolator.EASE_BOTH;
                          break;
                        case "linear":
                          newInterpolator = Interpolator.LINEAR;
                          break;
                        case "discrete":
                          newInterpolator = Interpolator.DISCRETE;
                          break;
                      }
                      interpolator.set(newInterpolator);
                    }
                  });

              StyleableProperty originalProperty = metaData.getStyleableProperty(styleable);
              if (originalProperty == null)
                originalProperty = parent.getStyleableProperty(styleable);

              PropertyInterpolator propertyInterpolator =
                  new PropertyInterpolator<>(
                      (ObservableValue) originalProperty,
                      (StyleableObjectProperty) transitionDuration.getStyleableProperty(styleable),
                      (StyleableObjectProperty) transitionDummy.getStyleableProperty(styleable),
                      interpolator);

              propertyAnimationTimers.add(propertyInterpolator.getAnimationTimer());
              return interpolatorProxy;
            }
          };

      McJfxGLControlBase.cssMetaData
          .get(getClass())
          .addAll(
              Arrays.asList(
                  (CssMetaData<? extends Styleable, ?>) transitionDuration,
                  (CssMetaData<? extends Styleable, ?>) transitionInterpolator));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected Skin<?> createDefaultSkin() {
    return null;
  }

  public Collection<CssMetaData<? extends Styleable, ?>> getControlClassMetaData() {
    return Control.getClassCssMetaData();
  }

  public final List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    return Collections.unmodifiableList(new ArrayList<>(cssMetaData.get(getClass())));
  }

  public void init(GuiAdapter adapter) {}

  public void render(GuiAdapter adapter) {
    if (this.defaultSkinClass == null) {
      this.defaultSkinClass = this.createDefaultSkin().getClass();
    }

    if (this.getSkin().getClass().equals(this.defaultSkinClass)) {
      Platform.runLater(
              () ->
                      this.setSkin(
                              InjectionHolder.getInjectedInstance(ThemeRepository.class)
                                      .getActive()
                                      .getSkin(this)));
    }
    for (PropertyAnimationTimer propertyAnimationTimer : this.propertyAnimationTimers) {
      if (propertyAnimationTimer.isRunning()) {
        propertyAnimationTimer.handle(System.nanoTime());
      }
    }
  }

  public McJfxGLComponent getComponent() {
    return component;
  }
}
