package net.flintmc.mcapi.v1_15_2.settings.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.settings.game.GameSettingsParser;
import net.flintmc.mcapi.settings.game.MinecraftConfiguration;
import net.flintmc.mcapi.version.VersionHelper;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Map;

@Singleton
public class VersionedGameSettingsInterceptor {

  private final ConfigStorageProvider storageProvider;
  private final MinecraftConfiguration configuration;
  private final GameSettingsParser gameSettingsParser;
  private final VersionHelper versionHelper;
  private Map<String, String> configurations;
  private File optionsFile;

  @Inject
  private VersionedGameSettingsInterceptor(
      ConfigStorageProvider storageProvider,
      MinecraftConfiguration configuration,
      GameSettingsParser gameSettingsParser,
      VersionHelper versionHelper) {
    this.storageProvider = storageProvider;
    this.configuration = configuration;
    this.gameSettingsParser = gameSettingsParser;
    this.versionHelper = versionHelper;
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void hookLoadOptions(OpenGLInitializeEvent event) {
    GameSettingsAccessor gameSettingsAccessor =
        (GameSettingsAccessor) Minecraft.getInstance().gameSettings;
    this.optionsFile = gameSettingsAccessor.getOptionsFile();
    this.configurations = this.gameSettingsParser.readOptions(this.optionsFile);

    if (this.configurations != null && (this.versionHelper.isUnder(13))) {
      this.gameSettingsParser.makeQualifiedKeyBinds(this.optionsFile, this.configurations);
    }
  }

  @Hook(className = "net.minecraft.client.GameSettings", methodName = "saveOptions")
  public void hookSaveOptions() {
    this.gameSettingsParser.saveOptions(this.optionsFile, this.configurations);

    // write the Flint config when an option is changed in the vanilla minecraft options screen
    this.storageProvider.write((ParsedConfig) this.configuration);
  }
}
