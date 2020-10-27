package net.flintmc.render.gui.webgui.internal;

import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.util.commons.Pair;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebFileSystemService;

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
