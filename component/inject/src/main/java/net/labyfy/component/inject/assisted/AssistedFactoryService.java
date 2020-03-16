package net.labyfy.component.inject.assisted;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;

import javax.inject.Singleton;

@Singleton
@Service(AssistedFactory.class)
public class AssistedFactoryService implements ServiceHandler {

  public void discover(Identifier.Base property) {
    InjectionHolder.getInstance()
        .addModules(
            new FactoryModuleBuilder()
                .build(
                    property
                        .getProperty()
                        .getLocatedIdentifiedAnnotation()
                        .<Class<?>>getLocation()));
  }
}
