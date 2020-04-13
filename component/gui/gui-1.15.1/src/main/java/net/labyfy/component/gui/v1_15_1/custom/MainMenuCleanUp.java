package net.labyfy.component.gui.v1_15_1.custom;

import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.asm.MethodVisit;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;

@Singleton
public class MainMenuCleanUp {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private MainMenuCleanUp(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @Gui(Guis.GUI_MAIN_MENU)
  @GuiRenderState(GuiRenderState.Type.INIT)
  public void modify(@Named("instance") Object mainMenuScreen) {
    ClassMapping classMapping = classMappingProvider.get("net.minecraft.client.gui.screen.Screen");

    List<Widget> buttons = classMapping.getField("buttons").getValue(mainMenuScreen);
    buttons.clear();

    classMapping
        .getField("children")
        .<Collection<IGuiEventListener>>getValue(mainMenuScreen)
        .clear();
  }

}
