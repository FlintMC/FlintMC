package net.flintmc.mcapi.v1_15_2.settings.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.mcapi.settings.game.MinecraftConfiguration;
import net.flintmc.mcapi.settings.game.configuration.AccessibilityConfiguration;
import net.flintmc.mcapi.settings.game.configuration.ChatConfiguration;
import net.flintmc.mcapi.settings.game.configuration.DebugConfiguration;
import net.flintmc.mcapi.settings.game.configuration.GraphicConfiguration;
import net.flintmc.mcapi.settings.game.configuration.KeyBindingConfiguration;
import net.flintmc.mcapi.settings.game.configuration.MouseConfiguration;
import net.flintmc.mcapi.settings.game.configuration.ResourcePackConfiguration;
import net.flintmc.mcapi.settings.game.configuration.SkinConfiguration;
import net.flintmc.mcapi.settings.game.configuration.SoundConfiguration;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;
import net.minecraft.client.Minecraft;

/** 1.15.2 implementation of {@link MinecraftConfiguration}. */
@Singleton
@ConfigInit(value = MinecraftInitializeEvent.class, eventPhase = Subscribe.Phase.POST)
@ConfigImplementation(value = MinecraftConfiguration.class, version = "1.15.2")
public class VersionedMinecraftConfiguration implements MinecraftConfiguration {

  private final AccessibilityConfiguration accessibilityConfiguration;
  private final ChatConfiguration chatConfiguration;
  private final DebugConfiguration debugConfiguration;
  private final GraphicConfiguration graphicConfiguration;
  private final KeyBindingConfiguration keyBindingConfiguration;
  private final MouseConfiguration mouseConfiguration;
  private final ResourcePackConfiguration resourcePackConfiguration;
  private final SkinConfiguration skinConfiguration;
  private final SoundConfiguration soundConfiguration;

  @Inject
  private VersionedMinecraftConfiguration(
      AccessibilityConfiguration accessibilityConfiguration,
      ChatConfiguration chatConfiguration,
      DebugConfiguration debugConfiguration,
      GraphicConfiguration graphicConfiguration,
      KeyBindingConfiguration keyBindingConfiguration,
      MouseConfiguration mouseConfiguration,
      ResourcePackConfiguration resourcePackConfiguration,
      SkinConfiguration skinConfiguration,
      SoundConfiguration soundConfiguration) {
    this.accessibilityConfiguration = accessibilityConfiguration;
    this.chatConfiguration = chatConfiguration;
    this.debugConfiguration = debugConfiguration;
    this.graphicConfiguration = graphicConfiguration;
    this.keyBindingConfiguration = keyBindingConfiguration;
    this.mouseConfiguration = mouseConfiguration;
    this.resourcePackConfiguration = resourcePackConfiguration;
    this.skinConfiguration = skinConfiguration;
    this.soundConfiguration = soundConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public AccessibilityConfiguration getAccessibilityConfiguration() {
    return this.accessibilityConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public ChatConfiguration getChatConfiguration() {
    return this.chatConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public DebugConfiguration getDebugConfiguration() {
    return this.debugConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public GraphicConfiguration getGraphicConfiguration() {
    return this.graphicConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public KeyBindingConfiguration getKeyBindingConfiguration() {
    return this.keyBindingConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public MouseConfiguration getMouseConfiguration() {
    return this.mouseConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public ResourcePackConfiguration getResourcePackConfiguration() {
    return this.resourcePackConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public SkinConfiguration getSkinConfiguration() {
    return this.skinConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public SoundConfiguration getSoundConfiguration() {
    return this.soundConfiguration;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRealmsNotifications() {
    return Minecraft.getInstance().gameSettings.realmsNotifications;
  }

  /** {@inheritDoc} */
  @Override
  public void setRealmsNotifications(boolean realmsNotifications) {
    Minecraft.getInstance().gameSettings.realmsNotifications = realmsNotifications;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public Difficulty getDifficulty() {
    switch (Minecraft.getInstance().gameSettings.difficulty) {
      case PEACEFUL:
        return Difficulty.PEACEFUL;
      case EASY:
        return Difficulty.EASY;
      case NORMAL:
        return Difficulty.NORMAL;
      case HARD:
        return Difficulty.HARD;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.difficulty);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setDifficulty(Difficulty difficulty) {
    switch (difficulty) {
      case PEACEFUL:
        Minecraft.getInstance().gameSettings.difficulty = net.minecraft.world.Difficulty.PEACEFUL;
        break;
      case EASY:
        Minecraft.getInstance().gameSettings.difficulty = net.minecraft.world.Difficulty.EASY;
        break;
      case NORMAL:
        Minecraft.getInstance().gameSettings.difficulty = net.minecraft.world.Difficulty.NORMAL;
        break;
      case HARD:
        Minecraft.getInstance().gameSettings.difficulty = net.minecraft.world.Difficulty.HARD;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + difficulty);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public void saveAndReloadOptions() {
    Minecraft.getInstance().gameSettings.saveOptions();
    Minecraft.getInstance().gameSettings.loadOptions();
  }
}
