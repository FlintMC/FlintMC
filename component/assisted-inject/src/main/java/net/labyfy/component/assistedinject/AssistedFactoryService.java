package net.labyfy.component.assistedinject;

import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.AbstractModule;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;

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
