package net.labyfy.internal.component.transform.minecraft;

import com.google.inject.Inject;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.transform.launchplugin.LabyfyLauncherPlugin;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;

import javax.inject.Singleton;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_TRANSFORMER_SERVICE_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_TRANSFORMER_SERVICE_ROUND;

@Singleton
@Service(value = MinecraftTransformer.class, priority = -10)
@AutoLoad(priority = MINECRAFT_TRANSFORMER_SERVICE_PRIORITY, round = MINECRAFT_TRANSFORMER_SERVICE_ROUND)
public class MinecraftTransformerService implements ServiceHandler<MinecraftTransformer> {


  @Inject
  private MinecraftTransformerService() {
  }

  @Override
  public void discover(IdentifierMeta<MinecraftTransformer> identifierMeta) throws ServiceNotFoundException {
    CtClass target = identifierMeta.<CtClass>getTarget();
    try {
      if (!target.subtypeOf(target.getClassPool().get(LateInjectedTransformer.class.getName()))) {
        throw new ServiceNotFoundException(new IllegalStateException("Class " + target.getName() + " does not implement " + LateInjectedTransformer.class.getName()));
      }
      LabyfyLauncherPlugin.getInstance()
          .registerTransformer(identifierMeta.getAnnotation().priority(), InjectionHolder.getInjectedInstance(CtResolver.get(identifierMeta.<CtClass>getTarget())));
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException(e);
    }
  }

}
