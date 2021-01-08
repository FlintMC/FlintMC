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

package net.flintmc.mcapi.v1_16_4.settings.game.configuration;

import com.google.inject.Singleton;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.settings.game.configuration.AccessibilityConfiguration;
import net.flintmc.mcapi.settings.game.settings.PointOfView;
import net.flintmc.mcapi.settings.game.settings.TutorialSteps;
import net.minecraft.client.Minecraft;

/**
 * 1.16.4 implementation of {@link AccessibilityConfiguration}.
 */
@Singleton
@ConfigImplementation(value = AccessibilityConfiguration.class, version = "1.16.4")
public class VersionedAccessibilityConfiguration implements AccessibilityConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHideServerAddress() {
    return Minecraft.getInstance().gameSettings.hideServerAddress;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHideServerAddress(boolean hideServerAddress) {
    Minecraft.getInstance().gameSettings.hideServerAddress = hideServerAddress;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAdvancedItemTooltips() {
    return Minecraft.getInstance().gameSettings.advancedItemTooltips;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAdvancedItemTooltips(boolean advancedItemTooltips) {
    Minecraft.getInstance().gameSettings.advancedItemTooltips = advancedItemTooltips;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPauseOnLostFocus() {
    return Minecraft.getInstance().gameSettings.pauseOnLostFocus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPauseOnLostFocus(boolean pauseOnLostFocus) {
    Minecraft.getInstance().gameSettings.pauseOnLostFocus = pauseOnLostFocus;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHeldItemTooltips() {
    return Minecraft.getInstance().gameSettings.heldItemTooltips;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeldItemTooltips(boolean heldItemTooltips) {
    Minecraft.getInstance().gameSettings.heldItemTooltips = heldItemTooltips;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUseNativeTransport() {
    return Minecraft.getInstance().gameSettings.useNativeTransport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUseNativeTransport(boolean useNativeTransport) {
    Minecraft.getInstance().gameSettings.useNativeTransport = useNativeTransport;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TutorialSteps getTutorialStep() {
    switch (Minecraft.getInstance().gameSettings.tutorialStep) {
      case MOVEMENT:
        return TutorialSteps.MOVEMENT;
      case FIND_TREE:
        return TutorialSteps.FIND_TREE;
      case PUNCH_TREE:
        return TutorialSteps.PUNCH_TREE;
      case OPEN_INVENTORY:
        return TutorialSteps.OPEN_INVENTORY;
      case CRAFT_PLANKS:
        return TutorialSteps.CRAFT_PLANKS;
      case NONE:
        return TutorialSteps.NONE;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.tutorialStep);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTutorialStep(TutorialSteps tutorialStep) {
    switch (tutorialStep) {
      case MOVEMENT:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.MOVEMENT;
        break;
      case FIND_TREE:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.FIND_TREE;
        break;
      case PUNCH_TREE:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.PUNCH_TREE;
        break;
      case OPEN_INVENTORY:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.OPEN_INVENTORY;
        break;
      case CRAFT_PLANKS:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.CRAFT_PLANKS;
        break;
      case NONE:
        Minecraft.getInstance().gameSettings.tutorialStep =
            net.minecraft.client.tutorial.TutorialSteps.NONE;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + tutorialStep);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PointOfView getPointOfView() {
    switch (Minecraft.getInstance().gameSettings.getPointOfView()) {
      case THIRD_PERSON_BACK:
        return PointOfView.THIRD_PERSON_BACK;
      case THIRD_PERSON_FRONT:
        return PointOfView.THIRD_PERSON_FRONT;
      default:
        return PointOfView.FIRST_PERSON;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPointOfView(PointOfView pointOfView) {
    switch (pointOfView) {
      case THIRD_PERSON_BACK:
        Minecraft.getInstance()
            .gameSettings
            .setPointOfView(net.minecraft.client.settings.PointOfView.THIRD_PERSON_BACK);
        break;
      case THIRD_PERSON_FRONT:
        Minecraft.getInstance()
            .gameSettings
            .setPointOfView(net.minecraft.client.settings.PointOfView.THIRD_PERSON_FRONT);
        break;
      default:
        Minecraft.getInstance()
            .gameSettings
            .setPointOfView(net.minecraft.client.settings.PointOfView.FIRST_PERSON);
        break;
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLastServer() {
    return Minecraft.getInstance().gameSettings.lastServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastServer(String lastServer) {
    Minecraft.getInstance().gameSettings.lastServer = lastServer;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSmoothCamera() {
    return Minecraft.getInstance().gameSettings.smoothCamera;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSmoothCamera(boolean smoothCamera) {
    Minecraft.getInstance().gameSettings.smoothCamera = smoothCamera;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLanguage() {
    return Minecraft.getInstance().gameSettings.language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLanguage(String language) {
    Minecraft.getInstance().gameSettings.language = language;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChunkSyncWrites() {
    return Minecraft.getInstance().gameSettings.syncChunkWrites;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setChunkSyncWrites(boolean chunkSyncWrites) {
    Minecraft.getInstance().gameSettings.syncChunkWrites = chunkSyncWrites;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isToggleCrouch() {
    return Minecraft.getInstance().gameSettings.toggleCrouch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setToggleCrouch(boolean toggleCrouch) {
    Minecraft.getInstance().gameSettings.toggleCrouch = toggleCrouch;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isToggleSprint() {
    return Minecraft.getInstance().gameSettings.toggleSprint;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setToggleSprint(boolean toggleSprint) {
    Minecraft.getInstance().gameSettings.toggleSprint = toggleSprint;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAutoJump() {
    return Minecraft.getInstance().gameSettings.autoJump;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAutoJump(boolean autoJump) {
    Minecraft.getInstance().gameSettings.autoJump = autoJump;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSnooper() {
    return Minecraft.getInstance().gameSettings.snooper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSnooper(boolean snooper) {
    Minecraft.getInstance().gameSettings.snooper = snooper;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSkipMultiplayerWarning() {
    return Minecraft.getInstance().gameSettings.skipMultiplayerWarning;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSkipMultiplayerWarning(boolean skipMultiplayerWarning) {
    Minecraft.getInstance().gameSettings.skipMultiplayerWarning = skipMultiplayerWarning;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowSubtitles() {
    return Minecraft.getInstance().gameSettings.showSubtitles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowSubtitles(boolean showSubtitles) {
    Minecraft.getInstance().gameSettings.showSubtitles = showSubtitles;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
