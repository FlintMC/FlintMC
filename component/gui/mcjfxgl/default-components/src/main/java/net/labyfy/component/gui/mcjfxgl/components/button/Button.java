package net.labyfy.component.gui.mcjfxgl.components.button;

import com.google.inject.assistedinject.AssistedInject;
import javafx.scene.control.Skin;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControl;
import net.labyfy.component.inject.assisted.AssistedFactory;

public class Button extends McJfxGLComponent {

  private final DefaultButtonSkin.Factory defaultSkinFactory;

  @AssistedInject
  private Button(DefaultButtonSkin.Factory defaultSkinFactory) {
    this.defaultSkinFactory = defaultSkinFactory;
  }

  public McJfxGLControl createControl() {
    return new Handle(this);
  }

  @AssistedFactory(Button.class)
  public interface Factory {
    Button create();
  }

  @IgnoreInitialization
  public static class Handle extends McJfxGLControl {

    private final Button button;

    private Handle(Button button) {
      super(button);
      this.button = button;
    }

    protected Skin<?> createDefaultSkin() {
      return button.defaultSkinFactory.create(button);
    }
  }
}
