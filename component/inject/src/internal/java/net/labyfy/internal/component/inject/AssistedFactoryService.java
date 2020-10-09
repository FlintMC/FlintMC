package net.labyfy.internal.component.inject;

import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import javax.inject.Singleton;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.ASSISTED_FACTORY_SERVICE_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.ASSISTED_FACTORY_SERVICE_ROUND;

/**
 * Service for automatically binding assisted factories.
 */
@Singleton
@Service(AssistedFactory.class)
@AutoLoad(priority = ASSISTED_FACTORY_SERVICE_PRIORITY, round = ASSISTED_FACTORY_SERVICE_ROUND)
public class AssistedFactoryService extends InjectionServiceShare implements ServiceHandler<AssistedFactory> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(IdentifierMeta<AssistedFactory> identifierMeta) throws ServiceNotFoundException {
    AssistedFactory annotation = identifierMeta.getAnnotation();
    assisted.put(identifierMeta.getTarget(), annotation);
    ignore.add(annotation.value());
  }



/*  public void discover(IdentifierLegacy.Base property) {
     AssistedFactory annotation =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    assisted.put(location, annotation);
    ignore.add(((AssistedFactory) annotation).value());
  }*/
}
