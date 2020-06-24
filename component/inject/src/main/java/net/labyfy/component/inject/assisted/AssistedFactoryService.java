package net.labyfy.component.inject.assisted;

import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.inject.InjectionServiceShare;


import javax.inject.Singleton;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

@Singleton
@Service(AssistedFactory.class)
@AutoLoad(priority = ASSISTED_FACTORY_SERVICE_PRIORITY, round = ASSISTED_FACTORY_SERVICE_ROUND)
public class AssistedFactoryService extends InjectionServiceShare implements ServiceHandler {



  public void discover(Identifier.Base property) {
     AssistedFactory annotation =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    assisted.put(location, annotation);

    ignore.add(((AssistedFactory) annotation).value());
  }

}
