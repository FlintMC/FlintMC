package net.flintmc.mcapi.v1_15_2.gamesettings.configuration;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.gamesettings.configuration.GraphicConfiguration;
import net.flintmc.mcapi.gamesettings.settings.*;
import net.minecraft.client.Minecraft;

/** 1.15.2 implementation of {@link GraphicConfiguration} */
@Singleton
@Implement(value = GraphicConfiguration.class, version = "1.15.2")
public class VersionedGraphicConfiguration implements GraphicConfiguration {

  /** {@inheritDoc} */
  @Override
  public int getRenderDistanceChunks() {
    return Minecraft.getInstance().gameSettings.renderDistanceChunks;
  }

  /** {@inheritDoc} */
  @Override
  public void setRenderDistanceChunks(int renderDistanceChunks) {
    Minecraft.getInstance().gameSettings.renderDistanceChunks = renderDistanceChunks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public float getEntityDistanceScaling() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public void setEntityDistanceScaling(float entityDistanceScaling) {
    // NO-OP
  }

  /** {@inheritDoc} */
  @Override
  public int getFramerateLimit() {
    return Minecraft.getInstance().gameSettings.framerateLimit;
  }

  /** {@inheritDoc} */
  @Override
  public void setFramerateLimit(int framerateLimit) {
    Minecraft.getInstance().gameSettings.framerateLimit = framerateLimit;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public CloudOption getCloudOption() {
    switch (Minecraft.getInstance().gameSettings.cloudOption) {
      case OFF:
        return CloudOption.OFF;
      case FAST:
        return CloudOption.FAST;
      case FANCY:
        return CloudOption.FANCY;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.cloudOption);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setCloudOption(CloudOption cloudOption) {
    switch (cloudOption) {
      case OFF:
        Minecraft.getInstance().gameSettings.cloudOption =
            net.minecraft.client.settings.CloudOption.OFF;
        break;
      case FAST:
        Minecraft.getInstance().gameSettings.cloudOption =
            net.minecraft.client.settings.CloudOption.FAST;
        break;
      case FANCY:
        Minecraft.getInstance().gameSettings.cloudOption =
            net.minecraft.client.settings.CloudOption.FANCY;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + cloudOption);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public GraphicsFanciness getGraphicsFanciness() {
    return Minecraft.getInstance().gameSettings.fancyGraphics
        ? GraphicsFanciness.FANCY
        : GraphicsFanciness.FAST;
  }

  /** {@inheritDoc} */
  @Override
  public void setGraphicsFanciness(GraphicsFanciness fancyGraphics) {
    Minecraft.getInstance().gameSettings.fancyGraphics = fancyGraphics != GraphicsFanciness.FAST;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public AmbientOcclusionStatus getAmbientOcclusionStatus() {
    switch (Minecraft.getInstance().gameSettings.ambientOcclusionStatus) {
      case OFF:
        return AmbientOcclusionStatus.OFF;
      case MIN:
        return AmbientOcclusionStatus.MIN;
      case MAX:
        return AmbientOcclusionStatus.MAX;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.ambientOcclusionStatus);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setAmbientOcclusionStatus(AmbientOcclusionStatus ambientOcclusionStatus) {
    switch (ambientOcclusionStatus) {
      case OFF:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus =
            net.minecraft.client.settings.AmbientOcclusionStatus.OFF;
        break;
      case MIN:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus =
            net.minecraft.client.settings.AmbientOcclusionStatus.MIN;
        break;
      case MAX:
        Minecraft.getInstance().gameSettings.ambientOcclusionStatus =
            net.minecraft.client.settings.AmbientOcclusionStatus.MAX;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + ambientOcclusionStatus);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public int getMipmapLevels() {
    return Minecraft.getInstance().gameSettings.mipmapLevels;
  }

  /** {@inheritDoc} */
  @Override
  public void setMipmapLevels(int mipmapLevels) {
    Minecraft.getInstance().gameSettings.mipmapLevels = mipmapLevels;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public AttackIndicatorStatus getAttackIndicator() {
    switch (Minecraft.getInstance().gameSettings.attackIndicator) {
      case OFF:
        return AttackIndicatorStatus.OFF;
      case CROSSHAIR:
        return AttackIndicatorStatus.CROSSHAIR;
      case HOTBAR:
        return AttackIndicatorStatus.HOTBAR;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.attackIndicator);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setAttackIndicator(AttackIndicatorStatus attackIndicator) {
    switch (attackIndicator) {
      case OFF:
        Minecraft.getInstance().gameSettings.attackIndicator =
            net.minecraft.client.settings.AttackIndicatorStatus.OFF;
        break;
      case CROSSHAIR:
        Minecraft.getInstance().gameSettings.attackIndicator =
            net.minecraft.client.settings.AttackIndicatorStatus.CROSSHAIR;
        break;
      case HOTBAR:
        Minecraft.getInstance().gameSettings.attackIndicator =
            net.minecraft.client.settings.AttackIndicatorStatus.HOTBAR;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + attackIndicator);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public int getBiomeBlendRadius() {
    return Minecraft.getInstance().gameSettings.biomeBlendRadius;
  }

  /** {@inheritDoc} */
  @Override
  public void setBiomeBlendRadius(int biomeBlendRadius) {
    Minecraft.getInstance().gameSettings.biomeBlendRadius = biomeBlendRadius;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isVsync() {
    return Minecraft.getInstance().gameSettings.vsync;
  }

  /** {@inheritDoc} */
  @Override
  public void setVsync(boolean vsync) {
    Minecraft.getInstance().gameSettings.vsync = vsync;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isFullscreen() {
    return Minecraft.getInstance().gameSettings.fullscreen;
  }

  /** {@inheritDoc} */
  @Override
  public void setFullscreen(boolean fullscreen) {
    Minecraft.getInstance().gameSettings.fullscreen = fullscreen;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isViewBobbing() {
    return Minecraft.getInstance().gameSettings.viewBobbing;
  }

  /** {@inheritDoc} */
  @Override
  public void setViewBobbing(boolean viewBobbing) {
    Minecraft.getInstance().gameSettings.viewBobbing = viewBobbing;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public double getFov() {
    return Minecraft.getInstance().gameSettings.fov;
  }

  /** {@inheritDoc} */
  @Override
  public void setFov(double fov) {
    Minecraft.getInstance().gameSettings.fov = fov;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public float getScreenEffectScale() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public void setScreenEffectScale(float screenEffectScale) {
    // NO-OP
  }

  /** {@inheritDoc} */
  @Override
  public float getFovEffectScale() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public void setFovEffectScale(float fovEffectScale) {
    // NO-OP
  }

  /** {@inheritDoc} */
  @Override
  public double getGamma() {
    return Minecraft.getInstance().gameSettings.gamma;
  }

  /** {@inheritDoc} */
  @Override
  public void setGamma(double gamma) {
    Minecraft.getInstance().gameSettings.gamma = gamma;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public int getGuiScale() {
    return Minecraft.getInstance().gameSettings.guiScale;
  }

  /** {@inheritDoc} */
  @Override
  public void setGuiScale(int guiScale) {
    Minecraft.getInstance().gameSettings.guiScale = guiScale;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public ParticleStatus getParticles() {
    switch (Minecraft.getInstance().gameSettings.particles) {
      case ALL:
        return ParticleStatus.ALL;
      case DECREASED:
        return ParticleStatus.DECREASED;
      case MINIMAL:
        return ParticleStatus.MINIMAL;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.particles);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setParticles(ParticleStatus particles) {
    switch (particles) {
      case ALL:
        Minecraft.getInstance().gameSettings.particles =
            net.minecraft.client.settings.ParticleStatus.ALL;
        break;
      case DECREASED:
        Minecraft.getInstance().gameSettings.particles =
            net.minecraft.client.settings.ParticleStatus.DECREASED;
        break;
      case MINIMAL:
        Minecraft.getInstance().gameSettings.particles =
            net.minecraft.client.settings.ParticleStatus.MINIMAL;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + particles);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEntityShadows() {
    return Minecraft.getInstance().gameSettings.entityShadows;
  }

  /** {@inheritDoc} */
  @Override
  public void setEntityShadows(boolean entityShadows) {
    Minecraft.getInstance().gameSettings.entityShadows = entityShadows;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public String getFullscreenResolution() {
    return Minecraft.getInstance().gameSettings.fullscreenResolution;
  }

  /** {@inheritDoc} */
  @Override
  public void setFullscreenResolution(String fullscreenResolution) {
    Minecraft.getInstance().gameSettings.fullscreenResolution = fullscreenResolution;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public int getOverrideWidth() {
    return Minecraft.getInstance().gameSettings.overrideWidth;
  }

  /** {@inheritDoc} */
  @Override
  public void setOverrideWidth(int overrideWidth) {
    Minecraft.getInstance().gameSettings.overrideWidth = overrideWidth;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public int getOverrideHeight() {
    return Minecraft.getInstance().gameSettings.overrideHeight;
  }

  /** {@inheritDoc} */
  @Override
  public void setOverrideHeight(int overrideHeight) {
    Minecraft.getInstance().gameSettings.overrideHeight = overrideHeight;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isHideGUI() {
    return Minecraft.getInstance().gameSettings.hideGUI;
  }

  /** {@inheritDoc} */
  @Override
  public void setHideGUI(boolean hideGUI) {
    Minecraft.getInstance().gameSettings.hideGUI = hideGUI;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isForceUnicodeFont() {
    return Minecraft.getInstance().gameSettings.forceUnicodeFont;
  }

  /** {@inheritDoc} */
  @Override
  public void setForceUnicodeFont(boolean forceUnicodeFont) {
    Minecraft.getInstance().gameSettings.forceUnicodeFont = forceUnicodeFont;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
