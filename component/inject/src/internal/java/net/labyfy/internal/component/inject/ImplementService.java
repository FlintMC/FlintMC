package net.labyfy.internal.component.inject;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

/**
 * Service for binding classes annotated with {@link Implement}.
 */
@Singleton
@Service(Implement.class)
@AutoLoad(priority = IMPLEMENT_SERVICE_PRIORITY, round = IMPLEMENT_SERVICE_ROUND)
public class ImplementService extends InjectionServiceShare implements ServiceHandler {
  private final Map<String, String> launchArguments;

  @Inject
  private ImplementService(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  /**
   * {@inheritDoc}
   */
  public void discover(Identifier.Base property) {
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    Implement annotation = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();

    if (!annotation.value().isAssignableFrom(location)) return;

    // TODO: --version is unreliable
    if (!(annotation.version().isEmpty()
        || launchArguments.get("--version").equals(annotation.version()))) return;

    implementations.put(annotation.value(), location);
  }
}
