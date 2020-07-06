package net.labyfy.internal.component.gui.juklearmc;

import net.labyfy.component.gui.juklearmc.JuklearScreen;
import net.labyfy.component.gui.juklearmc.menues.JuklearMCScreen;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles the discovery of Juklear screens.
 */
@Singleton
@Service(JuklearScreen.class)
public class JuklearScreenService implements ServiceHandler {
  private final DefaultJuklearMC juklearMC;

  @Inject
  private JuklearScreenService(DefaultJuklearMC juklearMC) {
    this.juklearMC = juklearMC;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(Identifier.Base property) {
    LocatedIdentifiedAnnotation annotation = property.getProperty().getLocatedIdentifiedAnnotation();

    // Construct the target screen name from the annotation values
    Class<? extends JuklearMCScreen> clazz = annotation.getLocation();
    JuklearScreen data = annotation.getAnnotation();
    ScreenName overwrittenName = ScreenName.typed(data.type(), data.value());

    // Add the initialization task so that once Juklear is initialized, the screen gets initialized
    juklearMC.onInitialize(
        () -> juklearMC.overwriteScreen(overwrittenName,  InjectionHolder.getInjectedInstance(clazz)));
  }
}
