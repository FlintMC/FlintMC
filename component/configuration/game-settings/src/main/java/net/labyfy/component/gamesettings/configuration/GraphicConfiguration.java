package net.labyfy.component.gamesettings.configuration;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.config.defval.annotation.DefaultBoolean;
import net.labyfy.component.config.defval.annotation.DefaultEnum;
import net.labyfy.component.config.defval.annotation.DefaultNumber;
import net.labyfy.component.gamesettings.settings.*;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.options.BooleanSetting;
import net.labyfy.component.settings.options.dropdown.CustomSelectSetting;
import net.labyfy.component.settings.options.dropdown.EnumSelectSetting;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;
import net.labyfy.component.settings.options.numeric.display.NumericDisplay;

/**
 * Represents the graphic configuration.
 */
@DefineCategory(
    name = "minecraft.settings.graphics",
    displayName = @Component(value = "minecraft.settings.graphics.display", translate = true),
    description = @Component(value = "minecraft.settings.graphics.description", translate = true)
)
@ImplementedConfig
public interface GraphicConfiguration {

  /**
   * Retrieves the gamma level.
   *
   * @return The gamma level.
   */
  double getGamma();

  /**
   * Changes the gamma level.
   *
   * @param gamma The new gamma level.
   */
  void setGamma(double gamma);

  /**
   * Retrieves the scale of the gui.
   *
   * @return The gui scale.
   */
  int getGuiScale();

  /**
   * Changes the gui scale.
   *
   * @param guiScale The new scale for the gui.
   */
  void setGuiScale(int guiScale);

  /**
   * Retrieves the status of particles.
   *
   * @return The particles status.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.particles", translate = true))
  @DefaultEnum(0 /* ALL */)
  ParticleStatus getParticles();

  /**
   * Changes the status of particles.
   *
   * @param particles The new particle status.
   */
  void setParticles(ParticleStatus particles);

  /**
   * Retrieves the field of view.
   *
   * @return The field of view.
   */
  @SliderSetting(@Range(min = 30, max = 110))
  @DisplayName(@Component(value = "options.fov", translate = true))
  @NumericDisplay(value = 70, display = @Component(value = "options.fov.min", translate = true))
  @NumericDisplay(value = 110, display = @Component(value = "options.fov.max", translate = true))
  double getFov();

  /**
   * Changes the field of view.
   *
   * @param fov The new field of view.
   */
  void setFov(double fov);

  /**
   * Retrieves the strength of nausea and nether portal screen distortion effects.
   *
   * @return The strength of nausea and nether portal screen distortion effects.
   */
  float getScreenEffectScale();

  /**
   * Changes the strength of nausea and nether portal screen distortions effects.
   *
   * @param screenEffectScale The new strength for the screen distortion effects.
   */
  void setScreenEffectScale(float screenEffectScale);

  /**
   * Retrieves how much the field of view can change with the speed effects.
   *
   * @return How much the field of view can change with the speed effects.
   */
  float getFovEffectScale();

  /**
   * Changes how much the field of view can change with the speed effects.
   *
   * @param fovEffectScale The new change for the field of view with the speed effects.
   */
  void setFovEffectScale(float fovEffectScale);

  /**
   * Whether the view is bobbing.
   *
   * @return {@code true} if the view is bobbing, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.viewBobbing", translate = true))
  @DefaultBoolean(true)
  boolean isViewBobbing();

  /**
   * Changes the state of the view bobbing.
   *
   * @param viewBobbing The new view bobbing state.
   */
  void setViewBobbing(boolean viewBobbing);

  /**
   * Whether the screen is in full screen mode.
   *
   * @return {@code true} if the screen is in full screen mode, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.fullscreen", translate = true))
  boolean isFullscreen();

  /**
   * Changes the full screen mode of the screen.
   *
   * @param fullscreen The new state for the screen.
   */
  void setFullscreen(boolean fullscreen);

  /**
   * Whether VSync(vertical synchronization) is enabled.
   *
   * @return {@code true} if VSync is enabled.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.vsync", translate = true))
  boolean isVsync();

  /**
   * Changes the VSync state.
   *
   * @param vsync The new VSync state.
   */
  void setVsync(boolean vsync);

  /**
   * Retrieves the blend radius for biomes.
   *
   * @return The biome blend radius.
   */
  @SliderSetting(@Range(max = 15))
  @DisplayName(@Component(value = "options.biomeBlendRadius", translate = true))
  int getBiomeBlendRadius();

  /**
   * Changes the blend radius for biomes.
   *
   * @param biomeBlendRadius The new biome blend radius.
   */
  void setBiomeBlendRadius(int biomeBlendRadius);

  /**
   * Retrieves the attack indicator status.
   *
   * @return The attack indicator status.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.attackIndicator", translate = true))
  @DefaultEnum(1 /* CROSSHAIR */)
  AttackIndicatorStatus getAttackIndicator();

  /**
   * Changes the attack indicator status.
   *
   * @param attackIndicator The new attack indicator status.
   */
  void setAttackIndicator(AttackIndicatorStatus attackIndicator);

  /**
   * Retrieves the mipmap levels.
   *
   * @return The mipmap levels.
   */
  @SliderSetting(@Range(max = 4))
  @DisplayName(@Component(value = "options.mipmapLevels", translate = true))
  int getMipmapLevels();

  /**
   * Changes the mipmap levels.
   *
   * @param mipmapLevels The new mipmap levels.
   */
  void setMipmapLevels(int mipmapLevels);

  /**
   * Retrieves the cloud option.
   *
   * @return The current cloud option.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.renderClouds", translate = true))
  @DefaultEnum(2 /* FANCY */)
  CloudOption getCloudOption();

  /**
   * Changes the cloud option.
   *
   * @param cloudOption The new cloud option.
   */
  void setCloudOption(CloudOption cloudOption);

  /**
   * Retrieves the graphic mode.
   *
   * @return The current graphic mode.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.graphics", translate = true))
  @DefaultEnum(1 /* FANCY */)
  GraphicsFanciness getGraphicsFanciness();

  /**
   * Changes the graphic mode.
   *
   * @param fancyGraphics The new graphic mode.
   */
  void setGraphicsFanciness(GraphicsFanciness fancyGraphics);

  /**
   * Retrieves the ambient occlusion status.
   *
   * @return The ambient occlusion status.
   */
  AmbientOcclusionStatus getAmbientOcclusionStatus();

  /**
   * Changes the ambient occlusion status.
   *
   * @param ambientOcclusionStatus The new ambient occlusion status.
   */
  void setAmbientOcclusionStatus(AmbientOcclusionStatus ambientOcclusionStatus);

  /**
   * Retrieves the render distance of chunks.
   *
   * @return The chunks render distance.
   */
  @SliderSetting(value = @Range(min = 2, max = 32))
  @DisplayName(@Component(value = "options.renderDistance", translate = true))
  @DefaultNumber(8)
  int getRenderDistanceChunks();

  /**
   * Changes the render distance of chunks.
   *
   * @param renderDistanceChunks The new chunks render distance.
   */
  void setRenderDistanceChunks(int renderDistanceChunks);

  /**
   * Retrieves the entity distance scaling.
   *
   * @return The entity distance scaling.
   */
  float getEntityDistanceScaling();

  /**
   * Changes the entity distance scaling.
   *
   * @param entityDistanceScaling The new entity distance scaling.
   */
  void setEntityDistanceScaling(float entityDistanceScaling);

  /**
   * Retrieves the limit of the framerate.
   *
   * @return The framerate limit.
   */
  @SliderSetting(@Range(min = 10, max = 260 /* 260 = unlimited */))
  @DisplayName(@Component(value = "options.framerateLimit", translate = true))
  int getFramerateLimit();

  /**
   * Changes the framerate limit.
   *
   * @param framerateLimit The new framerate limit.
   */
  void setFramerateLimit(int framerateLimit);

  /**
   * Retrieves the full screen resolution.
   *
   * @return The full screen resolution.
   */
  @CustomSelectSetting({})
  String getFullscreenResolution();

  /**
   * Changes the full screen resolution.
   *
   * @param fullscreenResolution The new full screen resolution.
   */
  void setFullscreenResolution(String fullscreenResolution);

  /**
   * Whether a shadow is rendered under an entity.
   *
   * @return {@code true} if a shadow is rendered under an entity.
   */
  @BooleanSetting
  @DefaultBoolean(true)
  boolean isEntityShadows();

  /**
   * Changes the state whether a shadow should be rendered under an entity.
   *
   * @param entityShadows The new state for the shadow.
   */
  void setEntityShadows(boolean entityShadows);

  /**
   * Retrieves the override width.
   *
   * @return The override width.
   */
  int getOverrideWidth();

  /**
   * Changes the override width.
   *
   * @param overrideWidth The new override width.
   */
  void setOverrideWidth(int overrideWidth);

  /**
   * Retrieves the override height.
   *
   * @return The override height.
   */
  int getOverrideHeight();

  /**
   * Changes the override height.
   *
   * @param overrideHeight The new override height.
   */
  void setOverrideHeight(int overrideHeight);

  /**
   * Whether the unicode font is forced.
   *
   * @return {@code true} if the unicode font is forced, otherwise {@code false}.
   */
  boolean isForceUnicodeFont();

  /**
   * Changes the state whether the unicode font is forced.
   *
   * @param forceUnicodeFont The new state.
   */
  void setForceUnicodeFont(boolean forceUnicodeFont);

  /**
   * Whether the GUI is hidden.
   *
   * @return {@code true} if the GUI is hidden, otherwise {@code false}.
   */
  boolean isHideGUI();

  /**
   * Changes the state whether the GUI is hidden.
   *
   * @param hideGUI The new state.
   */
  void setHideGUI(boolean hideGUI);

}
