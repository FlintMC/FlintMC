package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.settings.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DefineCategory;

/**
 * Represents the debug configuration.
 */
@DefineCategory(
    name = "minecraft.settings.debug",
    displayName = @Component(value = "minecraft.settings.debug.display", translate = true),
    description = @Component(value = "minecraft.settings.debug.description", translate = true)
)
@ImplementedConfig

// TODO add settings annotations

public interface DebugConfiguration {

  /**
   * Whether debug information is displayed.
   *
   * @return {@code true} if debug information is displayed, otherwise {@code false}.
   */
  boolean isShowDebugInfo();

  /**
   * Changes the state whether the debug information is displayed.
   *
   * @param showDebugInfo The new state.
   */
  void setShowDebugInfo(boolean showDebugInfo);

  /**
   * Whether the debug profiler chart is displayed.
   *
   * @return {@code true} if the debug profiler chart is displayed, otherwise {@code false}.
   */
  boolean isShowDebugProfilerChart();

  /**
   * Changes the state whether the debug profiler chart is displayed.
   *
   * @param showDebugProfilerChart The new state.
   */
  void setShowDebugProfilerChart(boolean showDebugProfilerChart);

  /**
   * Whether the lagometer is displayed.
   *
   * @return {@code true} if the lagometer is displayed, otherwise {@code false}.
   */
  boolean isShowLagometer();

  /**
   * Changes the state whether the lagometer is displayed.
   *
   * @param showLagometer The new state.
   */
  void setShowLagometer(boolean showLagometer);

  /**
   * Retrieves the verbosity GL debug level.
   *
   * @return The verbosity GL debug level.
   */
  int getGlDebugVerbosity();

  /**
   * Changes the verbosity GL debug level.
   *
   * @param glDebugVerbosity The new verbosity GL debug level.
   */
  void setGlDebugVerbosity(int glDebugVerbosity);
}
