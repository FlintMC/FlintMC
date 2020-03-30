package net.labyfy.component.gui.mcjfxgl.component.control.skin;

import javafx.scene.control.SkinBase;
import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControlBase;

public class McJfxGLControlBaseSkin<T extends McJfxGLControlBase<T>> extends SkinBase<T> {
  /**
   * Constructor for all SkinBase instances.
   *
   * @param control The control for which this Skin should attach to.
   */
  public McJfxGLControlBaseSkin(T control) {
    super(control);
  }
}
