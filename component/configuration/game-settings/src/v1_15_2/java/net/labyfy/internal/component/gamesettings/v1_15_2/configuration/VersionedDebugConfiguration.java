package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Singleton;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.gamesettings.configuration.DebugConfiguration;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of {@link DebugConfiguration}.
 */
@Singleton
@ConfigImplementation(value = DebugConfiguration.class, version = "1.15.2")
public class VersionedDebugConfiguration implements DebugConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGlDebugVerbosity() {
    return Minecraft.getInstance().gameSettings.glDebugVerbosity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGlDebugVerbosity(int glDebugVerbosity) {
    Minecraft.getInstance().gameSettings.glDebugVerbosity = glDebugVerbosity;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDebugInfo() {
    return Minecraft.getInstance().gameSettings.showDebugInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDebugInfo(boolean showDebugInfo) {
    Minecraft.getInstance().gameSettings.showDebugInfo = showDebugInfo;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDebugProfilerChart() {
    return Minecraft.getInstance().gameSettings.showDebugProfilerChart;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDebugProfilerChart(boolean showDebugProfilerChart) {
    Minecraft.getInstance().gameSettings.showDebugProfilerChart = showDebugProfilerChart;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowLagometer() {
    return Minecraft.getInstance().gameSettings.showLagometer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowLagometer(boolean showLagometer) {
    Minecraft.getInstance().gameSettings.showLagometer = showLagometer;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

}
