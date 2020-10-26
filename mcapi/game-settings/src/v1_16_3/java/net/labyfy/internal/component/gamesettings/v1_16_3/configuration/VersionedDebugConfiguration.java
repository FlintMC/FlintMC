package net.labyfy.internal.component.gamesettings.v1_16_3.configuration;

import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.configuration.DebugConfiguration;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;

/**
 * 1.16.3 implementation of {@link DebugConfiguration}.
 */
@Singleton
@Implement(value = DebugConfiguration.class, version = "1.16.3")
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
