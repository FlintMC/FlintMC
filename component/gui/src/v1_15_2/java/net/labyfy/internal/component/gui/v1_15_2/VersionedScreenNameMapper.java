package net.labyfy.internal.component.gui.v1_15_2;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.gui.screen.ScreenNameMapper;
import net.labyfy.component.inject.implement.Implement;

import java.util.Map;

@Singleton
@Implement(ScreenNameMapper.class)
public class VersionedScreenNameMapper implements ScreenNameMapper {
  // Map of all deobfuscated screen class names to their ScreeName equivalents
  private static final Map<String, ScreenName> KNOWN_NAMES = ImmutableMap.of(
      "net.minecraft.client.gui.screen.MainMenuScreen", ScreenName.minecraft(ScreenName.MAIN_MENU),
      "net.minecraft.client.gui.ResourceLoadProgressGui", ScreenName.minecraft(ScreenName.RESOURCE_LOAD),
      "net.minecraft.client.gui.screen", ScreenName.minecraft(ScreenName.OPTIONS),
      "net.minecraft.client.gui.screen.MultiplayerScreen", ScreenName.minecraft(ScreenName.MULTIPLAYER),
      "net.minecraft.client.gui.screen.WorldSelectionScreen", ScreenName.minecraft(ScreenName.SINGLEPLAYER)
  );

  /**
   * {@inheritDoc}
   */
  @Override
  public ScreenName fromClass(String className) {
    return KNOWN_NAMES.get(className);
  }
}
