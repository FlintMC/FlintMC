package net.labyfy.internal.component.settings.registered;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.event.ConfigValueUpdateEvent;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.event.subscribe.Subscribe.Phase;
import net.labyfy.component.settings.event.SettingsUpdateEvent;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;

@Singleton
public class RegisteredSettingUpdater {

  private final SettingsProvider provider;
  private final EventBus eventBus;
  private final SettingsUpdateEvent.Factory eventFactory;

  @Inject
  public RegisteredSettingUpdater(SettingsProvider provider, EventBus eventBus, SettingsUpdateEvent.Factory eventFactory) {
    this.provider = provider;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Subscribe(phase = Phase.PRE)
  public void preUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Phase.PRE);
  }

  @Subscribe(phase = Phase.POST)
  public void postUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Phase.POST);
  }

  private void fireEvent(ConfigObjectReference reference, Phase phase) {
    RegisteredSetting setting = this.provider.getSetting(reference);
    if (setting == null) {
      return;
    }

    SettingsUpdateEvent fired = this.eventFactory.create(setting);
    this.eventBus.fireEvent(fired, phase);
  }

}
