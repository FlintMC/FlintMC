package net.labyfy.component.gui.mcjfxgl.component.theme;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javafx.scene.control.Skin;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collections;
import java.util.Map;

public class Theme {

  private final Map<String, byte[]> content;
  private final ThemeConfig config;
  private final Map<Class<? extends McJfxGLControl>, ThemeComponentStyle> styleMap;

  @AssistedInject
  private Theme(
          @Assisted Map<String, byte[]> content,
          @Assisted ThemeConfig config,
          @Assisted Map<Class<? extends McJfxGLControl>, ThemeComponentStyle> styleMap) {
    this.content = content;
    this.config = config;
    this.styleMap = styleMap;
  }

  public Map<String, byte[]> getContent() {
    return Collections.unmodifiableMap(this.content);
  }

  public ThemeConfig getConfig() {
    return config;
  }

  public Map<Class<? extends McJfxGLControl>, ThemeComponentStyle> getStyleMap() {
    return styleMap;
  }

  public Skin<?> getSkin(McJfxGLControl controlBase) {
    return styleMap
            .get(controlBase.getComponent().getClass())
            .getApplyFunction()
            .apply(controlBase);
  }

  @AssistedFactory(Theme.class)
  public interface Factory {
    Theme create(
            Map<String, byte[]> content,
            ThemeConfig config,
            Map<Class<? extends McJfxGLControl>, ThemeComponentStyle> styleMap);
  }
}
