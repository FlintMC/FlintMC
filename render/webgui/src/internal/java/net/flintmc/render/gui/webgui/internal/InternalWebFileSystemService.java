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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javassist.CtClass;
import javassist.NotFoundException;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.commons.javassist.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.metaprogramming.identifier.ClassIdentifier;
import net.flintmc.render.gui.webgui.UnknownWebFileSystemException;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebFileSystemService;

@Singleton
@Implement(WebFileSystemService.class)
@Service(WebFileSystem.class)
public class InternalWebFileSystemService
    implements ServiceHandler<WebFileSystem>, WebFileSystemService {

  private final Map<String, CtClass> fileSystemsToLoad;
  private final Map<String, WebFileSystemHandler> fileSystemsLoaded;

  @Inject
  private InternalWebFileSystemService() {
    this.fileSystemsToLoad = new HashMap<>();
    this.fileSystemsLoaded = new HashMap<>();
  }

  @Override
  public synchronized void discover(AnnotationMeta<WebFileSystem> annotationMeta)
      throws ServiceNotFoundException {
    CtClass fileSystem = annotationMeta.<ClassIdentifier>getIdentifier().getLocation();

    try {
      if (Arrays.stream(fileSystem.getInterfaces())
          .noneMatch(iface -> iface.getName().equals(WebFileSystemHandler.class.getName()))) {
        throw new ServiceNotFoundException(
            "The filesystem doesn't implement the WebFileSystemHandler interface");
      }
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException(
          "Failed to read interfaces from discovered annotation bearer", e);
    }

    this.fileSystemsToLoad.put(annotationMeta.getAnnotation().value(), fileSystem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized WebFileSystemHandler getHandlerFor(String key) throws UnknownWebFileSystemException {
    WebFileSystemHandler handler;
    if((handler = this.fileSystemsLoaded.get(key)) != null) {
      // File system handler loaded already
      return handler;
    }

    CtClass pendingFileSystem;
    if((pendingFileSystem = this.fileSystemsToLoad.get(key)) == null) {
      // Don't know how to handle this file system type
      throw new UnknownWebFileSystemException("No file system handler available for " + key);
    }

    // Resolve the file system handler and cache it
    handler = InjectionHolder.getInjectedInstance(CtResolver.get(pendingFileSystem));
    this.fileSystemsLoaded.put(key, handler);
    return handler;
  }
}
