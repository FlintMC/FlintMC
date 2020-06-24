package net.labyfy.component.inject.implement;

import com.google.inject.Singleton;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.inject.InjectionServiceShare;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

@Singleton
@Service(Implement.class)
@AutoLoad(priority = IMPLEMENT_SERVICE_PRIORITY, round = IMPLEMENT_SERVICE_ROUND)
public class ImplementService extends InjectionServiceShare implements ServiceHandler {

  private final Map<String, String> launchArguments;


  @Inject
  private ImplementService(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  public void discover(Identifier.Base property) {
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    Implement annotation = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();

    if (!annotation.value().isAssignableFrom(location)) return;

    if (!(annotation.version().isEmpty()
        || launchArguments.get("--version").equals(annotation.version()))) return;

    implementations.put(annotation.value(), location);

  }

}
