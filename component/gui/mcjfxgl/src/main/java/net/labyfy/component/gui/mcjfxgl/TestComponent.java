package net.labyfy.component.gui.mcjfxgl;

import com.google.inject.assistedinject.AssistedInject;
import javafx.beans.property.StringProperty;
import javafx.css.StyleableStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.control.skin.McJfxGLControlBaseSkin;
import net.labyfy.component.gui.mcjfxgl.component.style.css.ResourceProvider;
import net.labyfy.component.inject.assisted.AssistedFactory;

@IgnoreInitialization
public class TestComponent extends McJfxGLControl<TestComponent> {

  private StyleableStringProperty textProperty;

  public String textProperty() {
    return textProperty.get();
  }

  @AssistedInject
  private TestComponent() {
  }

  @AssistedFactory(TestComponent.class)
  public interface Factory{
    TestComponent create();
  }
}
