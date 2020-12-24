/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.gui.webgui.internal;

import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebFileSystemService;
import net.flintmc.util.commons.Pair;

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
    CtClass fileSystem = annotationMeta.<ClassIdentifier>getIdentifier().getLocation();

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
