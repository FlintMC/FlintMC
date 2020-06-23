package net.labyfy.component.gui.juklearmc;

import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.inject.primitive.InjectionHolder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Service(JuklearScreen.class)
public class JuklearScreenService implements ServiceHandler {
  private final JuklearMC juklearMC;

  @Inject
  private JuklearScreenService(JuklearMC juklearMC) {
    this.juklearMC = juklearMC;
  }

  @Override
  public void discover(Identifier.Base property) {
    LocatedIdentifiedAnnotation annotation = property.getProperty().getLocatedIdentifiedAnnotation();

    Class<? extends JuklearMCScreen> clazz = annotation.getLocation();
    JuklearScreen data = annotation.getAnnotation();
    ScreenName overwrittenName = ScreenName.typed(data.type(), data.value());

    juklearMC.onInitialize(
        () -> juklearMC.overwriteScreen(overwrittenName,  InjectionHolder.getInjectedInstance(clazz)));
  }
}
