package net.labyfy.internal.component.gamesettings.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.gamesettings.GameSettingInterceptor;
import net.labyfy.component.gamesettings.GameSettingsAccessor;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.Map;

@Singleton
@AutoLoad
public class VersionedGameSettingsInterceptor {

  private final Map<String, String> launchArguments;
  private final GameSettingInterceptor gameSettingInterceptor;
  private Map<String, String> configurations;
  private File optionsFile;

  @Inject
  private VersionedGameSettingsInterceptor(@Named("launchArguments") Map launchArguments, GameSettingInterceptor gameSettingInterceptor) {
    this.launchArguments = launchArguments;
    this.gameSettingInterceptor = gameSettingInterceptor;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void hookLoadOptions() {
    GameSettingsAccessor gameSettingsAccessor = (GameSettingsAccessor) Minecraft.getInstance().gameSettings;
    this.optionsFile = gameSettingsAccessor.getOptionsFile();
    this.configurations = this.gameSettingInterceptor.readOptions(this.optionsFile);

    if (this.configurations != null && (this.gameSettingInterceptor.getMinorVersion(this.launchArguments.get("--game-version")) < 13)) {
      this.gameSettingInterceptor.makeQualifiedKeyBinds(this.optionsFile, this.configurations);
    }

  }

  @Hook(
          className = "net.minecraft.client.GameSettings",
          methodName = "saveOptions"
  )
  public void hookSaveOptions() {
    this.gameSettingInterceptor.saveOptions(this.optionsFile, this.configurations);
  }

}
