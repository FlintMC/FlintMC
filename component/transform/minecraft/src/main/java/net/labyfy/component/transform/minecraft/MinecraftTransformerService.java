package net.labyfy.component.transform.minecraft;

import com.google.inject.Inject;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.transform.launchplugin.LabyfyLauncherPlugin;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;

import javax.inject.Singleton;

@Singleton
@Service(value = MinecraftTransformer.class, priority = -10)
public class MinecraftTransformerService implements ServiceHandler {
  @Inject
  private MinecraftTransformerService() {}

  public void discover(Identifier.Base property) {
    LabyfyLauncherPlugin.getInstance()
        .registerTransformer(
            property
                .getProperty()
                .getLocatedIdentifiedAnnotation()
                .<MinecraftTransformer>getAnnotation()
                .priority(),
            InjectionHolder.getInjectedInstance(
                property
                    .getProperty()
                    .getLocatedIdentifiedAnnotation()
                    .<Class<? extends LateInjectedTransformer>>getLocation()));
  }
}
