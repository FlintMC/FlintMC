package net.labyfy.component.inject.assisted;

import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.inject.ServiceRepository;

import javax.inject.Singleton;

@Singleton
@Service(AssistedFactory.class)
@AutoLoad(priority = -1000, round = -4)
public class AssistedFactoryService extends InjectionServiceShare implements ServiceHandler {



  public void discover(Identifier.Base property) {
    AssistedFactory annotation =
        property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();

    assisted.put(location, annotation);

    ignore.add(((AssistedFactory) annotation).value());
  }

  @AutoLoad(priority = -1000)
  public static class Registrar{
    static {
      ServiceRepository.addPriorityService("net.labyfy.component.inject.assisted.AssistedFactoryService");
    }
  }
}
