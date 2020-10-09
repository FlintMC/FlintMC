package net.labyfy.internal.component.gamesettings.frontend;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.gamesettings.event.ConfigurationEvent;
import net.labyfy.component.gamesettings.frontend.FrontendCommunicator;
import net.labyfy.component.processing.autoload.AutoLoad;

/**
 * Default frontend listener.
 */
@Singleton
@AutoLoad
public class DefaultFrontendListener {

  private final FrontendCommunicator frontendCommunicator;

  @Inject
  private DefaultFrontendListener(FrontendCommunicator frontendCommunicator) {
    this.frontendCommunicator = frontendCommunicator;
  }

  @Subscribe
  @ConfigurationEvent.OptionState(ConfigurationEvent.State.LOAD)
  public void loadConfiguration(ConfigurationEvent event) {
    JsonObject object = this.frontendCommunicator.parseOption(event.getConfigurations());
    // TODO: 08.10.2020 Sends the object to the frontend
  }

  @Subscribe
  @ConfigurationEvent.OptionState(ConfigurationEvent.State.SAVE)
  public void saveConfiguration(ConfigurationEvent event) {
    // TODO: 09.10.2020 Fronted received logic
  }
}
