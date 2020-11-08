package net.flintmc.mcapi.internal.gamesettings;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.settings.game.GameSettingsParser;
import net.flintmc.mcapi.settings.game.event.ConfigurationEvent;
import net.flintmc.mcapi.settings.game.keybind.PhysicalKey;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/** Default implementation of the {@link GameSettingsParser}. */
@Singleton
@Implement(GameSettingsParser.class)
public class DefaultGameSettingsParser implements GameSettingsParser {

  private final Logger logger;
  private final EventBus eventBus;
  private final ConfigurationEvent.Factory configurationEventFactory;

  private File optionsFile;

  @Inject
  private DefaultGameSettingsParser(
      @InjectLogger Logger logger,
      EventBus eventBus,
      ConfigurationEvent.Factory configurationEventFactory) {
    this.logger = logger;
    this.eventBus = eventBus;
    this.configurationEventFactory = configurationEventFactory;
  }

  /** {@inheritDoc} */
  @Override
  public File getOptionsFile() {
    return this.optionsFile;
  }

  /** {@inheritDoc} */
  @Override
  public void makeQualifiedKeyBinds(File optionsFile, Map<String, String> configurations) {
    boolean savable = false;

    for (Map.Entry<String, String> entry : configurations.entrySet()) {
      if (entry.getKey().startsWith("key")) {

        int key = PhysicalKey.getScanCode(entry.getValue());

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

  /** {@inheritDoc} */
  @Override
  public Map<String, String> readOptions(File optionsFile) {
    this.optionsFile = optionsFile;
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
          this.configurationEventFactory.create(
              ConfigurationEvent.State.LOAD, optionsFile, configurations),
          Subscribe.Phase.PRE);

      bufferedReader.close();
    } catch (IOException exception) {
      this.logger.error(
          "Failed to read the minecraft options from " + optionsFile.getAbsolutePath(), exception);
      return null;
    }
    return configurations;
  }

  /** {@inheritDoc} */
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

  /** @see DefaultGameSettingsParser#saveOptions(File, Map) */
  private void save(File optionsFile, Map<String, String> configurations) {
    try {
      List<String> configuration = new ArrayList<>();

      for (Map.Entry<String, String> entry : configurations.entrySet()) {
        configuration.add(entry.getKey() + ":" + entry.getValue());
      }

      this.eventBus.fireEvent(
          this.configurationEventFactory.create(
              ConfigurationEvent.State.SAVE, optionsFile, configurations),
          Subscribe.Phase.PRE);

      Collections.sort(configuration);
      Files.write(optionsFile.toPath(), configuration, Charset.defaultCharset());
    } catch (IOException exception) {
      this.logger.error(
          "Failed to write the minecraft options to " + optionsFile.getAbsolutePath(), exception);
    }
  }
}
