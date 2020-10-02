package net.labyfy.internal.component.gamesettings.v1_15_2;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.gamesettings.frontend.FrontendCommunicator;
import net.labyfy.component.gamesettings.GameSettingInterceptor;
import net.labyfy.component.gamesettings.GameSettingsAccessor;
import net.labyfy.component.gamesettings.KeyBindMappings;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 1.15.2 implementation of {@link GameSettingInterceptor}.
 */
@Singleton
@Implement(value = GameSettingInterceptor.class, version = "1.15.2")
public class VersionedGameSettingsInterceptor implements GameSettingInterceptor {

  private final Map<String, String> launchArguments;
  private final FrontendCommunicator frontendCommunicator;
  private Map<String, String> configurations;
  private File optionsFile;

  private final JsonObject object;

  @Inject
  private VersionedGameSettingsInterceptor(@Named("launchArguments") Map launchArguments, FrontendCommunicator frontendCommunicator) {
    this.launchArguments = launchArguments;
    this.frontendCommunicator = frontendCommunicator;
    this.object = new JsonObject();
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void hookLoadOptions() {
    GameSettingsAccessor gameSettingsAccessor = (GameSettingsAccessor) Minecraft.getInstance().gameSettings;
    this.optionsFile = gameSettingsAccessor.getOptionsFile();
    this.configurations = this.readOptions();

    if (this.configurations != null && (this.getMinorVersion(this.launchArguments.get("--game-version")) < 13)) {
      this.makeQualifiedKeyBinds();
    }

  }

  @Hook(
          className = "net.minecraft.client.GameSettings",
          methodName = "saveOptions"
  )
  public void hookSaveOptions() {
    if (this.configurations == null) {
      return;
    }

    Map<String, String> configurations = Maps.newHashMap(this.configurations);
    Map<String, String> oldConfigurations = this.readOptions();

    if (oldConfigurations == null) {
      return;
    }

    for (Map.Entry<String, String> entry : configurations.entrySet()) {
      if (!oldConfigurations.containsKey(entry.getKey())) {
        oldConfigurations.put(entry.getKey(), entry.getValue());
      }
    }

    this.saveOptions(oldConfigurations);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeQualifiedKeyBinds() {
    boolean savable = false;

    for (Map.Entry<String, String> entry : this.configurations.entrySet()) {
      if (entry.getKey().startsWith("key")) {

        int key = KeyBindMappings.getScanCode(entry.getValue());

        if (key != -1) {
          this.configurations.put(entry.getKey(), String.valueOf(key));
          savable = true;
        }

      }
    }

    if (savable) {
      this.saveOptions(this.configurations);
    }

  }


  /**
   * {@inheritDoc}
   *
   * @return
   */
  @Override
  public Map<String, String> readOptions() {
    if (!this.optionsFile.exists()) {
      return null;
    }

    Map<String, String> configurations = new HashMap<>();

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(this.optionsFile));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] split = line.split(":", 2);

        if (split.length == 2) {
          configurations.put(split[0], split[1]);
        }
      }

      configurations.remove("version");
      bufferedReader.close();
    } catch (IOException exception) {
      exception.printStackTrace();
      return null;
    }
    return configurations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveOptions(Map<String, String> configurations) {
    try {
      List<String> configuration = new ArrayList<>();

      for (Map.Entry<String, String> entry : configurations.entrySet()) {
        configuration.add(entry.getKey() + ":" + entry.getValue());
      }

      Collections.sort(configuration);
      Files.write(this.optionsFile.toPath(), configuration, Charset.defaultCharset());
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void addProperty(JsonObject object, String name, Object value) {
    String convert = String.valueOf(value);

    try {
      int i = Integer.parseInt(convert);
      object.addProperty(name, i);
      return;
    } catch (NumberFormatException e) {

    }

    try {
      double d = Double.parseDouble(convert);
      object.addProperty(name, d);
      return;
    } catch (NumberFormatException e) {

    }
    if (convert.equalsIgnoreCase("true") || convert.equalsIgnoreCase("false")) {
      object.addProperty(name, Boolean.parseBoolean(convert));
    }
    /*
    if (value instanceof String) {
      object.addProperty(name, (String) value);
    } else if (value instanceof Boolean) {
      object.addProperty(name, (Boolean) value);
    } else if (value instanceof Number) {
      object.addProperty(name, (Number) value);
    } else if (value instanceof Character) {
      object.addProperty(name, (Character) value);
    }*/
  }

}
