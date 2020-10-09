package net.labyfy.internal.component.inject;

import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.IMPLEMENT_SERVICE_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.IMPLEMENT_SERVICE_ROUND;

/**
 * Service for binding classes annotated with {@link Implement}.
 */
@Singleton
@Service(Implement.class)
@AutoLoad(priority = IMPLEMENT_SERVICE_PRIORITY, round = IMPLEMENT_SERVICE_ROUND)
public class ImplementService extends InjectionServiceShare implements ServiceHandler<Implement> {
  private final Map<String, String> launchArguments;

  @Inject
  private ImplementService(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(IdentifierMeta<Implement> identifierMeta) throws ServiceNotFoundException {
    CtClass location = identifierMeta.getTarget();
    Implement annotation = identifierMeta.getAnnotation();

    if (!(annotation.version().isEmpty()
        || launchArguments.get("--game-version").equals(annotation.version()))) return;

    implementations.put(annotation.value(), location);
    implementationsReversed.put(location, annotation.value());
  }


}
