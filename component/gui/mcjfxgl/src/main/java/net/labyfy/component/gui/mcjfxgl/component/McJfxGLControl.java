package net.labyfy.component.gui.mcjfxgl.component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.DirtyBits;
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
import net.labyfy.component.gui.mcjfxgl.McJfxGLApplication;
import net.labyfy.component.gui.mcjfxgl.component.style.css.StyleableObjectPropertySelfProvidingCssMetaData;
import net.labyfy.component.gui.mcjfxgl.component.style.css.animate.PropertyAnimationTimer;
import net.labyfy.component.gui.mcjfxgl.component.style.css.interpolate.PropertyInterpolator;
import net.labyfy.component.gui.mcjfxgl.component.theme.Theme;
import net.labyfy.component.gui.mcjfxgl.component.theme.ThemeRepository;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;
import net.labyfy.component.inject.InjectionHolder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;

@IgnoreInitialization
public abstract class McJfxGLControl extends Control implements GuiComponent {

  private static final Map<
          Class<? extends McJfxGLControl>, Collection<CssMetaData<? extends Styleable, ?>>>
      cssMetaData = new HashMap<>();

  private static final Map<
          Class<? extends McJfxGLControl>, Collection<CssMetaData<? extends Styleable, ?>>>
      modifiedMetaData = new HashMap<>();

  private static final Map<String, String> MODIFY_PROPERTIES =
      new HashMap<>(
          ImmutableMap.<String, String>builder()
              .put("-fx-region-border", "-fx-border")
              .put("-fx-region-background", "-fx-background")
              .build());

  private final Collection<PropertyAnimationTimer> propertyAnimationTimers = new HashSet<>();
  private final McJfxGLComponent<?> component;
  private Class<?> defaultSkinClass;

  protected McJfxGLControl(McJfxGLComponent<?> component) {
    this.component = component;
    modifiedMetaData.putIfAbsent(getClass(), new CopyOnWriteArraySet<>());
    cssMetaData.putIfAbsent(getClass(), new CopyOnWriteArraySet<>());

    for (CssMetaData<? extends Styleable, ?> metaData : cssMetaData.get(getClass())) {
      if (!MODIFY_PROPERTIES.containsKey(metaData.getProperty())) {
        MODIFY_PROPERTIES.put(metaData.getProperty(), metaData.getProperty());
      }
      if (modifiedMetaData.get(getClass()).contains(metaData)) continue;
      addTransitionProperties(null, metaData, "transition-duration", "transition-interpolator");
      modifiedMetaData.get(getClass()).add(metaData);
    }

    this.translateXProperty().bindBidirectional(this.component.translateXProperty());
    this.translateYProperty().bindBidirectional(this.component.translateYProperty());

    this.maxWidthProperty().bindBidirectional(this.component.widthProperty());
    this.minWidthProperty().bindBidirectional(this.component.widthProperty());

    this.maxHeightProperty().bindBidirectional(this.component.heightProperty());
    this.minHeightProperty().bindBidirectional(this.component.heightProperty());

    this.backgroundProperty().bindBidirectional(this.component.backgroundProperty());

    Theme active = InjectionHolder.getInjectedInstance(ThemeRepository.class).getActive();
    if(active != null){
      this.setSkin(active.getSkin(this));
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

      McJfxGLControl.cssMetaData
          .get(getClass())
          .addAll(
              Arrays.asList(
                  (CssMetaData<? extends Styleable, ?>) transitionDuration,
                  (CssMetaData<? extends Styleable, ?>) transitionInterpolator));

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Deprecated
  public void impl_markDirty(DirtyBits dirtyBit) {
    super.impl_markDirty(dirtyBit);
  }

  protected Skin<?> createDefaultSkin() {
    return null;
  }

  public Collection<CssMetaData<? extends Styleable, ?>> getControlClassMetaData() {
    List<CssMetaData<?, ?>> meta = new ArrayList<>();
    meta.add(this.component.backgroundProperty().getCssMetaData());
    return meta;
  }

  public final List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    return Collections.unmodifiableList(
        Lists.newArrayList(
            Iterables.concat(cssMetaData.get(getClass()), getControlClassMetaData())));
  }

  public void init(GuiAdapter adapter) {
  }

  public void render(GuiAdapter adapter) {
    if (this.defaultSkinClass == null) {
      this.defaultSkinClass = this.getDefaultSkinClass();
    }

    if (this.getSkin() == null
        || (this.getSkin().getClass().equals(this.defaultSkinClass)
        && InjectionHolder.getInjectedInstance(ThemeRepository.class).getActive() != null)) {

      McJfxGLApplication.runAndWait(
          () -> {
            Theme active = InjectionHolder.getInjectedInstance(ThemeRepository.class).getActive();
            if (active == null) {
              if (this.getSkin() == null
                  || !this.getSkin().getClass().equals(this.defaultSkinClass)) {
                this.setSkin(this.createDefaultSkin());
              }
              return;
            }

            ThemeComponentStyle themeComponentStyle =
                active.getStyleMap().get(this.getComponent().getClass());

            if (themeComponentStyle == null) return;

            this.getStylesheets().clear();
            for (String styleSheet : themeComponentStyle.getStyleSheets()) {
              try {
                this.setStyle(IOUtils.toString(active.getContent().get(styleSheet), "utf-8"));
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            this.getStyleClass()
                .setAll(active.getStyleMap().get(this.getComponent().getClass()).getStyleClasses());
            Skin<?> skin = active.getSkin(this);
            if (skin == null) {
              if (this.getSkin() == null) {
                this.setSkin(this.createDefaultSkin());
              }
            } else {
              if (!this.getSkin().getClass().equals(skin.getClass())) {
                this.setSkin(skin);
              }
            }
          });

    }

    for (PropertyAnimationTimer propertyAnimationTimer : this.propertyAnimationTimers) {
      if (propertyAnimationTimer.isRunning()) {
        propertyAnimationTimer.handle(System.nanoTime());
      }
    }
  }

  protected abstract Class<? extends Skin<? extends McJfxGLControl>> getDefaultSkinClass();

  public McJfxGLComponent getComponent() {
    return component;
  }
}
