/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_5.settings.game;

import com.google.inject.Inject;
import com.google.inject.Provider;
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

/**
 * 1.16.5 implementation of {@link MinecraftConfiguration}.
 */
@Singleton
@ConfigInit(value = MinecraftInitializeEvent.class, eventPhase = Subscribe.Phase.POST)
@ConfigImplementation(value = MinecraftConfiguration.class)
public class VersionedMinecraftConfiguration implements MinecraftConfiguration {

  private final Provider<AccessibilityConfiguration> accessibilityConfiguration;
  private final Provider<ChatConfiguration> chatConfiguration;
  private final Provider<DebugConfiguration> debugConfiguration;
  private final Provider<GraphicConfiguration> graphicConfiguration;
  private final Provider<KeyBindingConfiguration> keyBindingConfiguration;
  private final Provider<MouseConfiguration> mouseConfiguration;
  private final Provider<ResourcePackConfiguration> resourcePackConfiguration;
  private final Provider<SkinConfiguration> skinConfiguration;
  private final Provider<SoundConfiguration> soundConfiguration;

  @Inject
  private VersionedMinecraftConfiguration(
      Provider<AccessibilityConfiguration> accessibilityConfiguration,
      Provider<ChatConfiguration> chatConfiguration,
      Provider<DebugConfiguration> debugConfiguration,
      Provider<GraphicConfiguration> graphicConfiguration,
      Provider<KeyBindingConfiguration> keyBindingConfiguration,
      Provider<MouseConfiguration> mouseConfiguration,
      Provider<ResourcePackConfiguration> resourcePackConfiguration,
      Provider<SkinConfiguration> skinConfiguration,
      Provider<SoundConfiguration> soundConfiguration) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public AccessibilityConfiguration getAccessibilityConfiguration() {
    return this.accessibilityConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatConfiguration getChatConfiguration() {
    return this.chatConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DebugConfiguration getDebugConfiguration() {
    return this.debugConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GraphicConfiguration getGraphicConfiguration() {
    return this.graphicConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KeyBindingConfiguration getKeyBindingConfiguration() {
    return this.keyBindingConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MouseConfiguration getMouseConfiguration() {
    return this.mouseConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourcePackConfiguration getResourcePackConfiguration() {
    return this.resourcePackConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinConfiguration getSkinConfiguration() {
    return this.skinConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SoundConfiguration getSoundConfiguration() {
    return this.soundConfiguration.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRealmsNotifications() {
    return Minecraft.getInstance().gameSettings.realmsNotifications;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRealmsNotifications(boolean realmsNotifications) {
    Minecraft.getInstance().gameSettings.realmsNotifications = realmsNotifications;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveAndReloadOptions() {
    Minecraft.getInstance().gameSettings.saveOptions();
    Minecraft.getInstance().gameSettings.loadOptions();
  }
}
