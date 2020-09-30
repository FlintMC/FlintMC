package net.labyfy.component.gamesettings;

import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;

import java.util.List;
import java.util.Set;

/**
 * Represents the Minecraft game settings
 */
public interface MinecraftConfiguration {

  boolean isHideServerAddress();

  void setHideServerAddress(boolean hideServerAddress);

  boolean isAdvancedItemTooltips();

  void setAdvancedItemTooltips(boolean advancedItemTooltips);

  boolean isPauseOnLostFocus();

  void setPauseOnLostFocus(boolean pauseOnLostFocus);

  int getOverrideWidth();

  void setOverrideWidth(int overrideWidth);

  int getOverrideHeight();

  void setOverrideHeight(int overrideHeight);

  boolean isHeldItemTooltips();

  void setHeldItemTooltips(boolean heldItemTooltips);

  boolean isUseNativeTransport();

  void setUseNativeTransport(boolean useNativeTransport);

  TutorialSteps getTutorialStep();

  void setTutorialStep(TutorialSteps tutorialStep);

  int getGlDebugVerbosity();

  void setGlDebugVerbosity(int glDebugVerbosity);

  boolean isForceUnicodeFont();

  void setForceUnicodeFont(boolean forceUnicodeFont);

  boolean isRealmsNotifications();

  void setRealmsNotifications(boolean realmsNotifications);

  boolean isSnooper();

  void setSnooper(boolean snooper);

  boolean isSkipMultiplayerWarning();

  void setSkipMultiplayerWarning(boolean skipMultiplayerWarning);

  // TODO: 28.09.2020 Wait for merge request #177 (Difficulty)
  Object getDifficulty();

  // TODO: 28.09.2020 Wait for merge request #177 (Difficulty)
  void setDifficulty(Object difficulty);

  boolean isHideGUI();

  void setHideGUI(boolean hideGUI);

  /**
   * 1.16.3
   */
  PointOfView getPointOfView();

  /**
   * 1.16.3
   */
  void setPointOfView(PointOfView pointOfView);

  boolean isShowDebugInfo();

  void setShowDebugInfo(boolean showDebugInfo);

  boolean isShowDebugProfilerChart();

  void setShowDebugProfilerChart(boolean showDebugProfilerChart);

  boolean isShowLagometer();

  void setShowLagometer(boolean showLagometer);

  String getLastServer();

  void setLastServer(String lastServer);

  boolean isSmoothCamera();

  void setSmoothCamera(boolean smoothCamera);

  String getLanguage();

  void setLanguage(String language);

  /**
   * 1.16.3
   */
  boolean isChunkSyncWrites();

  /**
   * 1.16.3
   */
  void setChunkSyncWrites(boolean chunkSyncWrites);

  float getMasterSoundVolume();

  void setMasterSoundVolume(float volume);

  float getMusicSoundVolume();

  void setMusicSoundVolume(float volume);

  float getRecordSoundVolume();

  void setRecordSoundVolume(float volume);

  float getWeatherSoundVolume();

  void setWeatherSoundVolume(float volume);

  float getBlockSoundVolume();

  void setBlockSoundVolume(float volume);

  float getHostileSoundVolume();

  void setHostileSoundVolume(float volume);

  float getNeutralSoundVolume();

  void setNeutralSoundVolume(float volume);

  float getPlayerSoundVolume();

  void setPlayerSoundVolume(float volume);

  float getAmbientSoundVolume();

  void setAmbientSoundVolume(float volume);

  float getVoiceSoundVolume();

  void setVoiceSoundVolume(float volume);

  boolean isShowSubtitles();

  void setShowSubtitles(boolean showSubtitles);

  double getGamma();

  void setGamma(double gamma);

  int getGuiScale();

  void setGuiScale(int guiScale);

  ParticleStatus getParticles();

  void setParticles(ParticleStatus particles);

  double getFov();

  void setFov(double fov);

  /**
   * 1.16.3
   */
  float getScreenEffectScale();

  /**
   * 1.16.3
   */
  void setScreenEffectScale(float screenEffectScale);

  /**
   * 1.16.3
   */
  float getFovEffectScale();

  void setFovEffectScale(float fovEffectScale);

  boolean isViewBobbing();

  void setViewBobbing(boolean viewBobbing);

  boolean isFullscreen();

  void setFullscreen(boolean fullscreen);

  boolean isVsync();

  void setVsync(boolean vsync);

  int getBiomeBlendRadius();

  void setBiomeBlendRadius(int biomeBlendRadius);

  AttackIndicatorStatus getAttackIndicator();

  void setAttackIndicator(AttackIndicatorStatus attackIndicator);

  int getMipmapLevels();

  void setMipmapLevels(int mipmapLevels);

  CloudOption getCloudOption();

  void setCloudOption(CloudOption cloudOption);

  GraphicsFanciness getGraphicsFanciness();

  void setGraphicsFanciness(GraphicsFanciness fancyGraphics);

  AmbientOcclusionStatus getAmbientOcclusionStatus();

  void setAmbientOcclusionStatus(AmbientOcclusionStatus ambientOcclusionStatus);

  int getRenderDistanceChunks();

  void setRenderDistanceChunks(int renderDistanceChunks);

  /**
   * 1.16.3
   */
  float getEntityDistanceScaling();

  /**
   * 1.16.3
   */
  void setEntityDistanceScaling(float entityDistanceScaling);

  int getFramerateLimit();

  void setFramerateLimit(int framerateLimit);

  String getFullscreenResolution();

  void setFullscreenResolution(String fullscreenResolution);

  boolean isEntityShadows();

  void setEntityShadows(boolean entityShadows);

  KeyBinding getKeyBindForward();

  KeyBinding getKeyBindLeft();

  KeyBinding getKeyBindBack();

  KeyBinding getKeyBindRight();

  KeyBinding getKeyBindJump();

  KeyBinding getKeyBindSneak();

  KeyBinding getKeyBindSprint();

  KeyBinding getKeyBindInventory();

  KeyBinding getKeyBindSwapHands();

  KeyBinding getKeyBindDrop();

  KeyBinding getKeyBindUseItem();

  KeyBinding getKeyBindAttack();

  KeyBinding getKeyBindPickBlock();

  KeyBinding getKeyBindChat();

  KeyBinding getKeyBindPlayerList();

  KeyBinding getKeyBindCommand();

  KeyBinding getKeyBindScreenshot();

  KeyBinding getKeyBindTogglePerspective();

  KeyBinding getKeyBindSmoothCamera();

  KeyBinding getKeyBindFullscreen();

  KeyBinding getKeyBindSpectatorOutlines();

  KeyBinding getKeyBindAdvancements();

  KeyBinding getKeyBindSaveToolbar();

  KeyBinding getKeyBindLoadToolbar();

  List<KeyBinding> getKeyBindsHotbar();

  List<KeyBinding> getKeyBindings();

  void setKeyBindingCode(KeyBinding bindingCode, String code);

  double getMouseSensitivity();

  void setMouseSensitivity(double mouseSensitivity);

  double getMouseWheelSensitivity();

  void setMouseWheelSensitivity(double mouseWheelSensitivity);

  boolean isRawMouseInput();

  void setRawMouseInput(boolean rawMouseInput);

  boolean isInvertMouse();

  void setInvertMouse(boolean invertMouse);

  boolean isDiscreteMouseScroll();

  void setDiscreteMouseScroll(boolean discreteMouseScroll);

  boolean isAutoJump();

  void setAutoJump(boolean autoJump);

  boolean isToggleCrouch();

  void setToggleCrouch(boolean toggleCrouch);

  boolean isToggleSprint();

  void setToggleSprint(boolean toggleSprint);

  boolean isTouchscreen();

  void setTouchscreen(boolean touchscreen);

  List<String> getResourcePacks();

  void setResourcePacks(List<String> resourcePacks);

  List<String> getIncompatibleResourcePacks();

  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);

  Hand.Side getMainHand();

  void setMainHand(Hand.Side mainHand);

  Set<PlayerClothing> getSetModelParts();

  void setModelClothingEnabled(PlayerClothing clothing, boolean enable);

  void switchModelClothingEnabled(PlayerClothing clothing);

  double getChatScale();

  void setChatScale(double chatScale);

  double getChatWidth();

  void setChatWidth(double chatWidth);

  double getChatHeightUnfocused();

  void setChatHeightUnfocused(double chatHeightUnfocused);

  double getChatHeightFocused();

  void setChatHeightFocused(double chatHeightFocused);

  /**
   * 1.16.3
   */
  double getChatDelay();

  /**
   * 1.16.3
   */
  void setChatDelay(double chatDelay);

  boolean isChatColor();

  void setChatColor(boolean chatColor);

  boolean isChatLinks();

  void setChatLinks(boolean chatLinks);

  boolean isChatLinksPrompt();

  void setChatLinksPrompt(boolean chatLinksPrompt);

  ChatVisibility getChatVisibility();

  void setChatVisibility(ChatVisibility chatVisibility);

  double getChatOpacity();

  void setChatOpacity(double chatOpacity);

  /**
   * 1.16.3
   */
  double getChatLineSpacing();

  /**
   * 1.16.3
   */
  void setChatLineSpacing(double chatLineSpacing);

  NarratorStatus getNarrator();

  void setNarrator(NarratorStatus narrator);

  boolean isAutoSuggestCommands();

  void setAutoSuggestCommands(boolean autoSuggestCommands);

  boolean isReducedDebugInfo();

  void setReducedDebugInfo(boolean reducedDebugInfo);

  double getAccessibilityTextBackgroundOpacity();

  void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity);

  boolean isAccessibilityTextBackground();

  void setAccessibilityTextBackground(boolean accessibilityTextBackground);
}