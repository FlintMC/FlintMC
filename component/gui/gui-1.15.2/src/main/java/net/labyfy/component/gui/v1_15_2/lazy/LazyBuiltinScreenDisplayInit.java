package net.labyfy.component.gui.v1_15_2.lazy;

import net.labyfy.component.gui.screen.ScreenName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;

import java.util.Map;
import java.util.function.Consumer;

public class LazyBuiltinScreenDisplayInit {
  public static void init(Map<ScreenName, Consumer<Object[]>> screens) {
    screens.put(ScreenName.minecraft(ScreenName.MAIN_MENU),
        (args) -> Minecraft.getInstance().displayGuiScreen(new MainMenuScreen()));
    screens.put(ScreenName.minecraft(ScreenName.OPTIONS),
        (args) -> Minecraft.getInstance().displayGuiScreen(new OptionsScreen(
            Minecraft.getInstance().currentScreen,
            Minecraft.getInstance().gameSettings
        )));
    screens.put(ScreenName.minecraft(ScreenName.MULTIPLAYER),
        (args) ->
            Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(Minecraft.getInstance().currentScreen)));
  }
}
