package net.labyfy.internal.component.gui.juklearmc;

import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import javax.inject.Inject;

/**
 * Handles the discovery of Juklear screens.
 * wasnt fixed, because it should be already removed.
 */
//@Singleton
//@Service(JuklearScreen.class)
public class JuklearScreenService implements ServiceHandler {
  private final DefaultJuklearMC juklearMC;

  @Inject
  private JuklearScreenService(DefaultJuklearMC juklearMC) {
    this.juklearMC = juklearMC;
  }

  @Override
  public void discover(IdentifierMeta identifierMeta) throws ServiceNotFoundException {

  }

  /**
   * {@inheritDoc}
   */
 /* @Override
  public void discover(IdentifierLegacy.Base property) {
    LocatedIdentifiedAnnotationLegacy annotation = property.getProperty().getLocatedIdentifiedAnnotation();

    // Construct the target screen name from the annotation values
    Class<? extends JuklearMCScreen> clazz = annotation.getLocation();
    JuklearScreen data = annotation.getAnnotation();
    ScreenName overwrittenName = ScreenName.typed(data.type(), data.value());

    // Add the initialization task so that once Juklear is initialized, the screen gets initialized
    juklearMC.onInitialize(
        () -> juklearMC.overwriteScreen(overwrittenName,  InjectionHolder.getInjectedInstance(clazz)));
  }*/
}
