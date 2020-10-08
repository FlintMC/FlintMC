package net.labyfy.internal.component.gamesettings;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.gamesettings.GameSettingInterceptor;
import net.labyfy.component.gamesettings.KeyBindMappings;
import net.labyfy.component.gamesettings.event.ConfigurationEvent;
import net.labyfy.component.inject.implement.Implement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

@Singleton
@Implement(GameSettingInterceptor.class)
public class DefaultGameSettingInterceptor implements GameSettingInterceptor {

  private final EventBus eventBus;
  private final ConfigurationEvent.Factory configurationEventFactory;
  private ConfigurationEvent configurationEvent;

  @Inject
  private DefaultGameSettingInterceptor(EventBus eventBus, ConfigurationEvent.Factory configurationEventFactory) {
    this.eventBus = eventBus;
    this.configurationEventFactory = configurationEventFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeQualifiedKeyBinds(File optionsFile, Map<String, String> configurations) {
    boolean savable = false;

    for (Map.Entry<String, String> entry : configurations.entrySet()) {
      if (entry.getKey().startsWith("key")) {

        int key = KeyBindMappings.getScanCode(entry.getValue());

        if (key != -1) {
          configurations.put(entry.getKey(), String.valueOf(key));
          savable = true;
        }

      }
    }

    if (savable) {
      this.saveOptions(optionsFile, configurations);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> readOptions(File optionsFile) {
    if (!optionsFile.exists()) {
      return null;
    }

    Map<String, String> configurations = new HashMap<>();

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(optionsFile));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] split = line.split(":", 2);

        if (split.length == 2) {
          configurations.put(split[0], split[1]);
        }
      }

      this.eventBus.fireEvent(
              this.createOrUpdateEvent(
                      ConfigurationEvent.State.LOAD,
                      optionsFile,
                      configurations
              ),
              Subscribe.Phase.ANY
      );

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
  public void saveOptions(File optionsFile, Map<String, String> configurations) {
    if (configurations == null) {
      return;
    }

    Map<String, String> hashedConfigurations = Maps.newHashMap(configurations);
    Map<String, String> oldConfigurations = this.readOptions(optionsFile);

    if (oldConfigurations == null) {
      return;
    }

    for (Map.Entry<String, String> entry : hashedConfigurations.entrySet()) {
      if (!oldConfigurations.containsKey(entry.getKey())) {
        oldConfigurations.put(entry.getKey(), entry.getValue());
      }
    }

    this.save(optionsFile, oldConfigurations);
  }

  /**
   * @see DefaultGameSettingInterceptor#saveOptions(File, Map)
   */
  private void save(File optionsFile, Map<String, String> configurations) {
    try {
      List<String> configuration = new ArrayList<>();

      for (Map.Entry<String, String> entry : configurations.entrySet()) {
        configuration.add(entry.getKey() + ":" + entry.getValue());
      }

      this.eventBus.fireEvent(
              this.createOrUpdateEvent(
                      ConfigurationEvent.State.SAVE,
                      optionsFile,
                      configurations
              ),
              Subscribe.Phase.ANY
      );

      Collections.sort(configuration);
      Files.write(optionsFile.toPath(), configuration, Charset.defaultCharset());
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Creates or updates the {@link #configurationEvent}.
   *
   * @param state          The state for the event.
   * @param optionsFile    The options file for the event.
   * @param configurations The configurations for the event.
   * @return A created or updated {@link ConfigurationEvent}.
   */
  private ConfigurationEvent createOrUpdateEvent(
          ConfigurationEvent.State state,
          File optionsFile,
          Map<String, String> configurations
  ) {
    if (this.configurationEvent == null) {
      this.configurationEvent = this.configurationEventFactory.create(state, optionsFile, configurations);
      return this.configurationEvent;
    }

    this.configurationEvent.setState(state);
    this.configurationEvent.setOptionsFile(optionsFile);
    this.configurationEvent.setConfigurations(configurations);

    return this.configurationEvent;
  }

}
