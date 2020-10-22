package net.labyfy.internal.component.gamesettings.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.GameSettingsParser;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.version.VersionHelper;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Map;

@Singleton
@AutoLoad
public class VersionedGameSettingsInterceptor {

  private final GameSettingsParser gameSettingsParser;
  private final VersionHelper versionHelper;
  private Map<String, String> configurations;
  private File optionsFile;

  @Inject
  private VersionedGameSettingsInterceptor(
          GameSettingsParser gameSettingsParser,
          VersionHelper versionHelper
  ) {
    this.versionHelper = versionHelper;
    this.gameSettingsParser = gameSettingsParser;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void hookLoadOptions() {
    GameSettingsAccessor gameSettingsAccessor = (GameSettingsAccessor) Minecraft.getInstance().gameSettings;
    this.optionsFile = gameSettingsAccessor.getOptionsFile();
    this.configurations = this.gameSettingsParser.readOptions(this.optionsFile);

    if (this.configurations != null && (this.versionHelper.isUnder(13))) {
      this.gameSettingsParser.makeQualifiedKeyBinds(this.optionsFile, this.configurations);
    }

  }

  @Hook(
          className = "net.minecraft.client.GameSettings",
          methodName = "saveOptions"
  )
  public void hookSaveOptions() {
    this.gameSettingsParser.saveOptions(this.optionsFile, this.configurations);
  }

}
