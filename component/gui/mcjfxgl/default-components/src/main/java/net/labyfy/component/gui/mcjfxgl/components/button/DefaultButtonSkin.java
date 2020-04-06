package net.labyfy.component.gui.mcjfxgl.components.button;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.resources.ResourceLocationProvider;

@IgnoreInitialization
public class DefaultButtonSkin extends SkinBase<Control> {

  private final ResourceLocationProvider resourceLocationProvider;

  @AssistedInject
  private DefaultButtonSkin(
          @Assisted Button button, ResourceLocationProvider resourceLocationProvider) {
    super(button.getControl());
    this.resourceLocationProvider = resourceLocationProvider;
  }

  @AssistedFactory(DefaultButtonSkin.class)
  public interface Factory {
    DefaultButtonSkin create(Button button);
  }
}
