package net.labyfy.internal.component.gamesettings.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.GameSettings;
import net.labyfy.component.configuration.ConfigurationService;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.hook.Hook;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * NOT FINAL ONLY FOR TESTING
 *
 */
@Singleton
@AutoLoad
public class VersionedConfigurationInjector {

  private final ConfigurationService configurationService;
  private final GameSettings gameSettings;
  private final Logger logger;

  @Inject
  public VersionedConfigurationInjector(ConfigurationService configurationService, GameSettings gameSettings, Logger logger) {
    this.configurationService = configurationService;
    this.gameSettings = gameSettings;
    this.logger = logger;
  }

  @Hook(
          className = "net.minecraft.client.GameSettings",
          methodName = "saveOptions"
  )
  public void saveConfiguration() {
    this.configurationService.save(this.gameSettings.getClass().getInterfaces()[0]);
    this.logger.info("Configuration saved!");
  }

  @Hook(
          executionTime = Hook.ExecutionTime.AFTER,
          className = "net.minecraft.client.GameSettings",
          methodName = "loadOptions"
  )
  public void loadConfiguration() {
    this.configurationService.load(this.gameSettings.getClass().getInterfaces()[0]);
    this.logger.info("Configuration loaded!");
  }

}
