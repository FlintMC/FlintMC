package net.flintmc.mcapi.internal.settings.flint.registered;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.settings.flint.event.SettingsUpdateEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

@Singleton
public class RegisteredSettingUpdater {

  private final SettingsProvider provider;
  private final EventBus eventBus;
  private final SettingsUpdateEvent.Factory eventFactory;

  @Inject
  public RegisteredSettingUpdater(
      SettingsProvider provider, EventBus eventBus, SettingsUpdateEvent.Factory eventFactory) {
    this.provider = provider;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Subscribe(phase = Subscribe.Phase.PRE)
  public void preUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Subscribe.Phase.PRE);
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void postUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Subscribe.Phase.POST);
  }

  private void fireEvent(ConfigObjectReference reference, Subscribe.Phase phase) {
    RegisteredSetting setting = this.provider.getSetting(reference);
    if (setting == null) {
      return;
    }

    SettingsUpdateEvent fired = this.eventFactory.create(setting);
    this.eventBus.fireEvent(fired, phase);
  }
}
