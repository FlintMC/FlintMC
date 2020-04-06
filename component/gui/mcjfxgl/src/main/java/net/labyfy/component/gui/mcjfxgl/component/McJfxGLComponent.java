package net.labyfy.component.gui.mcjfxgl.component;

import net.labyfy.component.gui.mcjfxgl.component.control.McJfxGLControl;

public abstract class McJfxGLComponent {

  private McJfxGLControl control;

  public abstract McJfxGLControl createControl();

  public final McJfxGLControl getControl() {
    if (this.control == null) this.control = this.createControl();
    return this.control;
  }
}
