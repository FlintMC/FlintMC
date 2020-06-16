package net.labyfy.component.gui.v1_15_2;

import com.google.common.collect.ImmutableMap;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.gui.screen.ScreenNameMapper;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
@Implement(ScreenNameMapper.class)
public class LabyScreenNameMapper implements ScreenNameMapper {
  private static final Map<String, ScreenName> KNOWN_NAMES = ImmutableMap.of(
      "net.minecraft.client.gui.screen.MainMenuScreen", ScreenName.minecraft(ScreenName.MAIN_MENU),
      "net.minecraft.client.gui.ResourceLoadProgressGui", ScreenName.minecraft(ScreenName.RESOURCE_LOAD),
      "net.minecraft.client.gui.screen", ScreenName.minecraft(ScreenName.OPTIONS),
      "net.minecraft.client.gui.screen.MultiplayerScreen", ScreenName.minecraft(ScreenName.MULTIPLAYER)
  );

  @Override
  public ScreenName fromClass(String className) {
    return KNOWN_NAMES.get(className);
  }
}
