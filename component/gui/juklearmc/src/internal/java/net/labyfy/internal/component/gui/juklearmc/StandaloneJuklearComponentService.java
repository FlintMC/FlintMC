package net.labyfy.internal.component.gui.juklearmc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.juklearmc.StandaloneJuklearComponent;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCComponent;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

@Singleton
@Service(StandaloneJuklearComponent.class)
public class StandaloneJuklearComponentService implements ServiceHandler {

  private final DefaultJuklearMC defaultJuklearMC;

  @Inject
  private StandaloneJuklearComponentService(DefaultJuklearMC defaultJuklearMC) {
    this.defaultJuklearMC = defaultJuklearMC;
  }

  public void discover(Identifier.Base property) {
    this.defaultJuklearMC.onInitialize(
        () -> this.defaultJuklearMC
            .registerStandaloneComponent(
                InjectionHolder.getInjectedInstance(property.getProperty().getLocatedIdentifiedAnnotation().<Class<? extends JuklearMCComponent>>getLocation())));
  }
}
