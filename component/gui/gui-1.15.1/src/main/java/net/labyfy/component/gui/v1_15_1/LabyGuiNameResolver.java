package net.labyfy.component.gui.v1_15_1;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import net.labyfy.component.gui.GuiNameResolver;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.inject.implement.Implement;

import java.util.Map;

@Singleton
@Implement(value = GuiNameResolver.class, version = "1.15.1")
public class LabyGuiNameResolver implements GuiNameResolver {

  private final Map<String, String> guiNames =
      ImmutableMap.of(
          Guis.GUI_MAIN_MENU,
          "net.minecraft.client.gui.screen.MainMenuScreen",
          Guis.GUI_CLASS,
          "net.minecraft.client.gui.screen.Screen",
          Guis.GUI_BOOT,
          "net.minecraft.client.gui.ResourceLoadProgressGui");

  public String resolve(String name) {
    return this.guiNames.getOrDefault(name, name);
  }
}
