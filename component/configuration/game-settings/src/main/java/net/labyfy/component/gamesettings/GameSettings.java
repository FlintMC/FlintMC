package net.labyfy.component.gamesettings;

import net.labyfy.component.configuration.Configuration;
import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.labyfy.component.player.util.sound.SoundCategory;

import java.util.List;
import java.util.Set;

/**
 * Represents the Minecraft game settings
 */
@Configuration(value = "labyfy/minecraft/minecraft_options")
public interface GameSettings {

  @Configuration.Entry("mouseSensitivity")
  double getMouseSensitivity();

  @Configuration.Entry("mouseSensitivity")
  void setMouseSensitivity(double mouseSensitivity);

  @Configuration.Entry("renderDistanceChunks")
  int getRenderDistanceChunks();

  @Configuration.Entry("renderDistanceChunks")
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

  CloudOption getCloudOption();

  void setCloudOption(CloudOption cloudOption);

  GraphicsFanciness getGraphicsFanciness();

  void setGraphicsFanciness(GraphicsFanciness fancyGraphics);

  AmbientOcclusionStatus getAmbientOcclusionStatus();

  void setAmbientOcclusionStatus(AmbientOcclusionStatus ambientOcclusionStatus);

  List<String> getResourcePacks();

  void setResourcePacks(List<String> resourcePacks);

  List<String> getIncompatibleResourcePacks();

  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);

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

  double getAccessibilityTextBackgroundOpacity();

  void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity);

  String getFullscreenResolution();

  void setFullscreenResolution(String fullscreenResolution);

  boolean isHideServerAddress();

  void setHideServerAddress(boolean hideServerAddress);

  boolean isAdvancedItemTooltips();

  void setAdvancedItemTooltips(boolean advancedItemTooltips);

  boolean isPauseOnLostFocus();

  void setPauseOnLostFocus(boolean pauseOnLostFocus);

  Set<PlayerClothing> getSetModelParts();

  void setModelClothingEnabled(PlayerClothing clothing, boolean enable);

  void switchModelClothingEnabled(PlayerClothing clothing);

  Hand.Side getMainHand();

  void setMainHand(Hand.Side mainHand);

  int getOverrideWidth();

  void setOverrideWidth(int overrideWidth);

  int getOverrideHeight();

  void setOverrideHeight(int overrideHeight);

  boolean isHeldItemTooltips();

  void setHeldItemTooltips(boolean heldItemTooltips);

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

  int getMipmapLevels();

  void setMipmapLevels(int mipmapLevels);

  float getSoundVolume(SoundCategory category);

  void setSoundVolume(SoundCategory category, float volume);

  boolean isUseNativeTransport();

  void setUseNativeTransport(boolean useNativeTransport);

  AttackIndicatorStatus getAttackIndicator();

  void setAttackIndicator(AttackIndicatorStatus attackIndicator);

  TutorialSteps getTutorialStep();

  void setTutorialStep(TutorialSteps tutorialStep);

  int getBiomeBlendRadius();

  void setBiomeBlendRadius(int biomeBlendRadius);

  double getMouseWheelSensitivity();

  void setMouseWheelSensitivity(double mouseWheelSensitivity);

  boolean isRawMouseInput();

  void setRawMouseInput(boolean rawMouseInput);

  int getGlDebugVerbosity();

  void setGlDebugVerbosity(int glDebugVerbosity);

  boolean isAutoJump();

  void setAutoJump(boolean autoJump);

  boolean isAutoSuggestCommands();

  void setAutoSuggestCommands(boolean autoSuggestCommands);

  boolean isChatColor();

  void setChatColor(boolean chatColor);

  boolean isChatLinks();

  void setChatLinks(boolean chatLinks);

  boolean isChatLinksPrompt();

  void setChatLinksPrompt(boolean chatLinksPrompt);

  boolean isVsync();

  void setVsync(boolean vsync);

  boolean isEntityShadows();

  void setEntityShadows(boolean entityShadows);

  boolean isForceUnicodeFont();

  void setForceUnicodeFont(boolean forceUnicodeFont);

  boolean isInvertMouse();

  void setInvertMouse(boolean invertMouse);

  boolean isDiscreteMouseScroll();

  void setDiscreteMouseScroll(boolean discreteMouseScroll);

  boolean isRealmsNotifications();

  void setRealmsNotifications(boolean realmsNotifications);

  boolean isReducedDebugInfo();

  void setReducedDebugInfo(boolean reducedDebugInfo);

  boolean isSnooper();

  void setSnooper(boolean snooper);

  boolean isShowSubtitles();

  void setShowSubtitles(boolean showSubtitles);

  boolean isAccessibilityTextBackground();

  void setAccessibilityTextBackground(boolean accessibilityTextBackground);

  boolean isTouchscreen();

  void setTouchscreen(boolean touchscreen);

  boolean isFullscreen();

  void setFullscreen(boolean fullscreen);

  boolean isViewBobbing();

  void setViewBobbing(boolean viewBobbing);

  boolean isToggleCrouch();

  void setToggleCrouch(boolean toggleCrouch);

  boolean isToggleSprint();

  void setToggleSprint(boolean toggleSprint);

  boolean isSkipMultiplayerWarning();

  void setSkipMultiplayerWarning(boolean skipMultiplayerWarning);

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

  List<KeyBinding> getKeyBindsHotbar();

  KeyBinding getKeyBindSaveToolbar();

  KeyBinding getKeyBindLoadToolbar();

  List<KeyBinding> getKeyBindings();

  void setKeyBindingCode(KeyBinding bindingCode, String code);

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

  double getGamma();

  void setGamma(double gamma);

  int getGuiScale();

  void setGuiScale(int guiScale);

  ParticleStatus getParticles();

  void setParticles(ParticleStatus particles);

  NarratorStatus getNarrator();

  void setNarrator(NarratorStatus narrator);

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

}