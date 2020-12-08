package net.flintmc.mcapi.v1_15_2.settings.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.settings.game.MinecraftConfiguration;
import net.flintmc.mcapi.settings.game.event.GameSettingsStorageEvent;
import net.flintmc.mcapi.settings.game.event.GameSettingsStorageEvent.State;
import net.flintmc.transform.hook.Hook;

@Singleton
public class VersionedGameSettingsInterceptor {

  private final ConfigStorageProvider storageProvider;
  private final MinecraftConfiguration configuration;
  private final EventBus eventBus;
  private final GameSettingsStorageEvent loadEvent;
  private final GameSettingsStorageEvent saveEvent;

  @Inject
  private VersionedGameSettingsInterceptor(
      ConfigStorageProvider storageProvider,
      MinecraftConfiguration configuration,
      EventBus eventBus,
      GameSettingsStorageEvent.Factory eventFactory) {
    this.storageProvider = storageProvider;
    this.configuration = configuration;
    this.eventBus = eventBus;
    this.loadEvent = eventFactory.create(State.LOAD);
    this.saveEvent = eventFactory.create(State.SAVE);
  }

  @Hook(
      className = "net.minecraft.client.GameSettings",
      methodName = "loadOptions",
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void hookLoadOptions(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.loadEvent, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.GameSettings",
      methodName = "saveOptions",
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void hookSaveOptions(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.saveEvent, executionTime);

    if (executionTime == Hook.ExecutionTime.AFTER) {
      // write the Flint config when an option is changed in the vanilla minecraft options screen
      this.storageProvider.write((ParsedConfig) this.configuration);
    }
  }
}
