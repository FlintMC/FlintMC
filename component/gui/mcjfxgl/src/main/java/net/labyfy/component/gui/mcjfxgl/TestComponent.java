package net.labyfy.component.gui.mcjfxgl;

import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.control.skin.McJfxGLControlBaseSkin;
import net.labyfy.component.gui.mcjfxgl.component.style.css.ResourceProvider;

@IgnoreInitialization
public class TestComponent extends McJfxGLControl<TestComponent> {

  protected Skin<?> createDefaultSkin() {
    return new McJfxGLControlBaseSkin<TestComponent>(this){
      {
        this.getChildren().add(new Button("Test123"));
        getSkinnable().getStyleClass().add("test");
        getSkinnable().getStylesheets().add(ResourceProvider.getCSSResource("test.css"));
      }
    };
  }
}
