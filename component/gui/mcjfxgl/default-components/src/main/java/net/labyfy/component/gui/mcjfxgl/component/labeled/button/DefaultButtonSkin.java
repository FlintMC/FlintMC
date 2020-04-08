package net.labyfy.component.gui.mcjfxgl.component.labeled.button;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocationProvider;

@IgnoreInitialization
public class DefaultButtonSkin extends SkinBase<Button.Handle> {

  @AssistedInject
  private DefaultButtonSkin(
          @Assisted Button component, ResourceLocationProvider resourceLocationProvider) {
    super((Button.Handle) component.getControl());

    Text text = new Text();
    text.textProperty().bind(component.textProperty());
    text.fontProperty().bind(component.textFontProperty());
    text.fillProperty().bind(component.textFillProperty());
    this.getChildren().add(text);
  }

  @AssistedFactory(DefaultButtonSkin.class)
  public interface Factory {
    DefaultButtonSkin create(Button component);
  }
}
