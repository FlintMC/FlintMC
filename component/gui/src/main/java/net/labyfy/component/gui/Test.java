package net.labyfy.component.gui;

public class Test {

  @Gui(Guis.GUI_MAIN_MENU)
  @GuiRenderState(GuiRenderState.Type.INIT)
  public void transform() {
    System.out.println("Transform");
  }
}
