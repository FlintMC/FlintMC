package net.flintmc.mcapi.v1_15_2.gamesettings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.mcapi.gamesettings.GameSettingsParser;
import net.flintmc.mcapi.version.VersionHelper;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Map;

@Singleton
public class VersionedGameSettingsInterceptor {

  private final GameSettingsParser gameSettingsParser;
  private final VersionHelper versionHelper;
  private Map<String, String> configurations;
  private File optionsFile;

  @Inject
  private VersionedGameSettingsInterceptor(
      GameSettingsParser gameSettingsParser, VersionHelper versionHelper) {
    this.gameSettingsParser = gameSettingsParser;
    this.versionHelper = versionHelper;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void hookLoadOptions() {
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
  }
}
