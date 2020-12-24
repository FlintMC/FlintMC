package net.flintmc.render.gui.v1_16_4.lazy;

import java.util.Map;
import java.util.function.Consumer;
import net.flintmc.render.gui.screen.ScreenName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;

/**
 * Helper classes used for deferred to loading to prevent class transform issues due to early
 * loading.
 */
public class VersionedBuiltinScreenDisplayInit {

  /**
   * Initializes the given map with the given screen instantiators.
   *
   * @param screens The map to fill with the instantiators
   */
  public static void init(Map<ScreenName, Consumer<Object[]>> screens) {
    // TODO: Handle passing arguments to the screen constructors
    screens.put(
        ScreenName.minecraft(ScreenName.MAIN_MENU),
        (args) -> Minecraft.getInstance().displayGuiScreen(new MainMenuScreen()));
    screens.put(
        ScreenName.minecraft(ScreenName.OPTIONS),
        (args) ->
            Minecraft.getInstance()
                .displayGuiScreen(
                    new OptionsScreen(
                        Minecraft.getInstance().currentScreen,
                        Minecraft.getInstance().gameSettings)));
    screens.put(
        ScreenName.minecraft(ScreenName.MULTIPLAYER),
        (args) ->
            Minecraft.getInstance()
                .displayGuiScreen(new MultiplayerScreen(Minecraft.getInstance().currentScreen)));
    screens.put(
        ScreenName.minecraft(ScreenName.SINGLEPLAYER),
        (args) ->
            Minecraft.getInstance()
                .displayGuiScreen(new WorldSelectionScreen(Minecraft.getInstance().currentScreen)));
  }
}
