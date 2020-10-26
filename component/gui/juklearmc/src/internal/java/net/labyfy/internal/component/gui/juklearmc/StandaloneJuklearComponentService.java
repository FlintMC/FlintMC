package net.labyfy.internal.component.gui.juklearmc;

import com.google.inject.Inject;
import net.labyfy.component.gui.juklearmc.StandaloneJuklearComponent;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

/**
 * Not fixed because should be removed already.
 */
//@Singleton
//@Service(StandaloneJuklearComponent.class)
public class StandaloneJuklearComponentService implements ServiceHandler<StandaloneJuklearComponent> {

  private final DefaultJuklearMC defaultJuklearMC;

  @Inject
  private StandaloneJuklearComponentService(DefaultJuklearMC defaultJuklearMC) {
    this.defaultJuklearMC = defaultJuklearMC;
  }

  @Override
  public void discover(AnnotationMeta<StandaloneJuklearComponent> identifierMeta) throws ServiceNotFoundException {

  }

//  public void discover(IdentifierLegacy.Base property) {
//    this.defaultJuklearMC.onInitialize(
//        () -> this.defaultJuklearMC
//            .registerStandaloneComponent(
//                InjectionHolder.getInjectedInstance(property.getProperty().getLocatedIdentifiedAnnotation().<Class<? extends JuklearMCComponent>>getLocation())));
//  }
}
