package net.labyfy.internal.webgui;

import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.ClassIdentifier;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.webgui.WebFileSystem;
import net.labyfy.webgui.WebFileSystemHandler;
import net.labyfy.webgui.WebFileSystemService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Implement(WebFileSystemService.class)
@Service(WebFileSystem.class)
public class InternalWebFileSystemService
    implements ServiceHandler<WebFileSystem>, WebFileSystemService {

  private final List<Pair<CtClass, String>> fileSystems;
  private List<Pair<WebFileSystemHandler, String>> cachedInstances;

  @Inject
  private InternalWebFileSystemService() {
    this.fileSystems = new ArrayList<>();
  }

  @Override
  public void discover(AnnotationMeta<WebFileSystem> annotationMeta)
      throws ServiceNotFoundException {

    CtClass fileSystem =
        annotationMeta.<ClassIdentifier>getIdentifier().getLocation();

    try {
      if (Arrays.stream(fileSystem.getInterfaces())
          .noneMatch(iface -> iface.getName().equals(WebFileSystemHandler.class.getName()))) {
        throw new ServiceNotFoundException(
            "The filesystem doesn't implement the WebFileSystemHandler interface.");
      }
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException(
          "Failed to read interfaces from discovered annotation bearer.", e);
    }

    fileSystems.add(new Pair<>(fileSystem, annotationMeta.getAnnotation().value()));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Pair<WebFileSystemHandler, String>> getFileSystems() {
    if (cachedInstances == null) {
      cachedInstances =
          fileSystems.stream()
              .map(
                  ctPair ->
                      new Pair<WebFileSystemHandler, String>(
                          InjectionHolder.getInjectedInstance(CtResolver.get(ctPair.first())),
                          ctPair.second()))
              .collect(Collectors.toList());
    }
    return cachedInstances;
  }
}
