package net.labyfy.component.gui.v1_15_1.custom;

import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.mappings.ClassMapping;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;

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

    classMapping.getField("buttons").<Collection<Widget>>getValue(mainMenuScreen).clear();

    classMapping
            .getField("children")
            .<Collection<IGuiEventListener>>getValue(mainMenuScreen)
            .clear();
  }


}
