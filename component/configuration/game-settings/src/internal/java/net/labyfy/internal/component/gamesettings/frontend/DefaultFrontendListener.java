package net.labyfy.internal.component.gamesettings.frontend;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.gamesettings.event.ConfigurationEvent;
import net.labyfy.component.gamesettings.frontend.FrontendCommunicator;
import net.labyfy.component.processing.autoload.AutoLoad;

@Singleton
@AutoLoad
public class DefaultFrontendListener {

  private final FrontendCommunicator frontendCommunicator;

  @Inject
  private DefaultFrontendListener(FrontendCommunicator frontendCommunicator) {
    this.frontendCommunicator = frontendCommunicator;
  }

  @Subscribe(phase = Subscribe.Phase.ANY)
  public void configuration(ConfigurationEvent event) {
    switch (event.getState()) {
      case LOAD:
        JsonObject object = this.frontendCommunicator.parseOption(event.getConfigurations());
        // TODO: 08.10.2020 Sends the object to the frontend
        break;
      case SAVE:
        // TODO: 08.10.2020 Frontend received logic
        break;
    }
  }

}
