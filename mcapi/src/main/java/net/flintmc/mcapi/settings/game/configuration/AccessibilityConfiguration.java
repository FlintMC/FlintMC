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

package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.options.bool.BooleanSetting;
import net.flintmc.mcapi.settings.game.annotation.LanguageSetting;
import net.flintmc.mcapi.settings.game.settings.PointOfView;
import net.flintmc.mcapi.settings.game.settings.TutorialSteps;

/**
 * Represents the accessibility configuration.
 */
@DefineCategory(
    name = "minecraft.settings.accessiblity",
    displayName = @Component(value = "options.accessibility.title", translate = true))
@ImplementedConfig
public interface AccessibilityConfiguration {

  /**
   * Whether automatic jumping is activated.
   *
   * @return {@code true} if automatic jumping is activated, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.autoJump", translate = true))
  boolean isAutoJump();

  /**
   * Changes the state for the auto jump.
   *
   * @param autoJump The new state for the auto jump.
   */
  void setAutoJump(boolean autoJump);

  /**
   * Whether the crouch is toggled.
   *
   * @return {@code true} if the crouch is toggled, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isToggleCrouch();

  /**
   * Changes the state for the toggle crouch.
   *
   * @param toggleCrouch The new state for toggle crouch.
   */
  void setToggleCrouch(boolean toggleCrouch);

  /**
   * Whether the sprint is toggled.
   *
   * @return {@code true} if the sprint is toggled, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isToggleSprint();

  /**
   * Changes the state for the toggle sprint.
   *
   * @param toggleSprint The new state for the toggle sprint.
   */
  void setToggleSprint(boolean toggleSprint);

  /**
   * Whether the server address is hidden.
   *
   * @return {@code true} if the server address is hidden, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isHideServerAddress();

  /**
   * Changes the state to hide the server address.
   *
   * @param hideServerAddress The new state to hide the server address.
   */
  void setHideServerAddress(boolean hideServerAddress);

  /**
   * Whether the advanced item tooltips is activated. In game this can be toggled with F3 + H.
   *
   * @return {@code true} if the advanced item tooltips is activated.
   */
  boolean isAdvancedItemTooltips();

  /**
   * Changes the state for the advanced item tooltips. In game this can be toggled with F3 + H.
   *
   * @param advancedItemTooltips The new state for the advanced item tooltips.
   */
  void setAdvancedItemTooltips(boolean advancedItemTooltips);

  /**
   * Whether the focus should be lost during the pause. In game this can be toggled with F3 + P.
   *
   * @return {@code true} if the focus should be lost during the pause, otherwise {@code false}.
   */
  boolean isPauseOnLostFocus();

  /**
   * Changes the state to lost the focus on pause. In game this can be toggled with F3 + P.
   *
   * @param pauseOnLostFocus The new state for the pause.
   */
  void setPauseOnLostFocus(boolean pauseOnLostFocus);

  /**
   * Whether it is a smooth camera.
   *
   * @return {@code true} if it is a smooth camera, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isSmoothCamera();

  /**
   * Changes the state of the smooth camera.
   *
   * @param smoothCamera The new state for the smooth camera.
   */
  void setSmoothCamera(boolean smoothCamera);

  /**
   * Whether to display tooltips for held objects.
   *
   * @return {@code true} if tooltips for held objects is displayed, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isHeldItemTooltips();

  /**
   * Changes the state for held item tooltips.
   *
   * @param heldItemTooltips The new state for held item tooltips.
   */
  void setHeldItemTooltips(boolean heldItemTooltips);

  /**
   * Whether the native transport can be used.
   *
   * @return {@code true} if the native transport can be used, otherwise {@code false}.
   */
  boolean isUseNativeTransport();

  /**
   * Changes whether the native transport should be used.
   *
   * @param useNativeTransport The new state for the native transport.
   */
  void setUseNativeTransport(boolean useNativeTransport);

  /**
   * Retrieves the tutorial steps.
   *
   * @return The tutorial steps.
   */
  TutorialSteps getTutorialStep();

  /**
   * Changes the tutorial steps.
   *
   * @param tutorialStep The new tutorial steps.
   */
  void setTutorialStep(TutorialSteps tutorialStep);

  /**
   * Whether the snooper is activated.
   *
   * @return {@code true} if the snooper is activated, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.snooper", translate = true))
  boolean isSnooper();

  /**
   * Changes the state for the snooper.
   *
   * @param snooper The new snooper state.
   */
  void setSnooper(boolean snooper);

  /**
   * Whether the multiplayer warning can be skipped.
   *
   * @return {@code true} if the multiplayer warning can be skipped.
   */
  boolean isSkipMultiplayerWarning();

  /**
   * Changes the state for skipping the multiplayer warning.
   *
   * @param skipMultiplayerWarning The new state for skipping the multiplayer warning.
   */
  void setSkipMultiplayerWarning(boolean skipMultiplayerWarning);

  /**
   * Retrieves the point of view.
   *
   * @return The point of view.
   */
  PointOfView getPointOfView();

  /**
   * Changes the point of view.
   *
   * @param pointOfView The new point of view.
   */
  void setPointOfView(PointOfView pointOfView);

  /**
   * Retrieves the last server.
   *
   * @return The last server.
   */
  String getLastServer();

  /**
   * Changes the last server.
   *
   * @param lastServer The new last server.
   */
  void setLastServer(String lastServer);

  /**
   * Retrieves the current language.
   *
   * @return The current language.
   */
  @LanguageSetting
  @DisplayName(@Component(value = "options.language", translate = true))
  String getLanguage();

  /**
   * Changes the language.
   *
   * @param language The new language.
   */
  void setLanguage(String language);

  /**
   * Whether chunks are written synchronously.
   *
   * @return {@code true} if chunks are written synchronously, otherwise {@code false}.
   */
  boolean isChunkSyncWrites();

  /**
   * Changes of the state for chunks are written synchronously.
   *
   * @param chunkSyncWrites The new state.
   */
  void setChunkSyncWrites(boolean chunkSyncWrites);

  /**
   * Whether subtitles should be displayed.
   *
   * @return {@code true} if subtitles should be displayed, otherwise {@code false}.
   */
  @DisplayName(@Component(value = "options.showSubtitles", translate = true))
  boolean isShowSubtitles();

  /**
   * Changes whether subtitles should be displayed.
   *
   * @param showSubtitles The new state for subtitles.
   */
  void setShowSubtitles(boolean showSubtitles);
}
