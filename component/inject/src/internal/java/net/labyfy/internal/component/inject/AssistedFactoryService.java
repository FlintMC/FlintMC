package net.labyfy.internal.component.inject;

import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;


import javax.inject.Singleton;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

/**
 * Service for automatically binding assisted factories.
 */
@Singleton
@Service(AssistedFactory.class)
@AutoLoad(priority = ASSISTED_FACTORY_SERVICE_PRIORITY, round = ASSISTED_FACTORY_SERVICE_ROUND)
public class AssistedFactoryService extends InjectionServiceShare implements ServiceHandler {
  /**
   * {@inheritDoc}
   */
  public void discover(Identifier.Base property) {
     AssistedFactory annotation =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    assisted.put(location, annotation);
    ignore.add(((AssistedFactory) annotation).value());
  }
}
