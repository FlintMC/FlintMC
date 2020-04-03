package net.labyfy.component.gui.mcjfxgl.component.theme;

import com.google.common.base.Preconditions;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControlBase;
import net.labyfy.component.gui.mcjfxgl.component.theme.style.ThemeComponentStyle;

import java.util.Map;

public class Theme {

  private final ThemeConfig config;
  private final Map<Class<? extends McJfxGLControlBase<?>>, ThemeComponentStyle> styleMap;

  protected Theme(
      ThemeConfig config,
      Map<Class<? extends McJfxGLControlBase<?>>, ThemeComponentStyle> styleMap) {
    this.config = config;
    this.styleMap = styleMap;
  }

  public ThemeConfig getConfig() {
    return config;
  }

  public Map<Class<? extends McJfxGLControlBase<?>>, ThemeComponentStyle> getStyleMap() {
    return styleMap;
  }

  public Skin<?> getSkin(McJfxGLControlBase<?> controlBase) {
    return styleMap.get(controlBase.getClass()).getApplyFunction().apply(controlBase);
  }
}
