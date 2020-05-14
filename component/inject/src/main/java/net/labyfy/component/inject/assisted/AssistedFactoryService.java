package net.labyfy.component.inject.assisted;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionServiceShare;

import javax.inject.Singleton;

@Singleton
@Service(AssistedFactory.class)
public class AssistedFactoryService extends InjectionServiceShare implements ServiceHandler {

  public void discover(Identifier.Base property) {
    AssistedFactory annotation =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    assisted.put(location, annotation);
    ignore.add(((AssistedFactory) annotation).value());
  }
}
