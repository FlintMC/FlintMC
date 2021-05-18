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

package net.flintmc.transform.shadow.internal;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Collection;
import java.util.Map;
import javassist.CtClass;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.launcher.classloading.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.shadow.Shadow;
import net.flintmc.transform.shadow.internal.handler.RegisteredShadowHandler;
import net.flintmc.transform.shadow.internal.handler.ShadowHandlerService;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.apache.logging.log4j.Logger;

@Singleton
@Service(value = Shadow.class, priority = -20000, state = Service.State.AFTER_IMPLEMENT)
public class ShadowService implements ServiceHandler<Shadow> {

  private final Logger logger;
  private final ClassMappingProvider classMappingProvider;
  private final ShadowHandlerService service;
  private final Multimap<String, AnnotationMeta<Shadow>> transforms;

  @Inject
  private ShadowService(
      @InjectLogger Logger logger,
      ClassMappingProvider classMappingProvider,
      ShadowHandlerService service) {
    this.logger = logger;
    this.classMappingProvider = classMappingProvider;
    this.service = service;
    this.transforms = HashMultimap.create();
  }

  @Override
  public void discover(AnnotationMeta<Shadow> meta)
      throws ServiceNotFoundException {
    CtClass location = meta.getClassIdentifier().getLocation();
    String target = meta.getAnnotation().value();

    if (!location.isInterface()) {
      throw new ServiceNotFoundException(
          String.format(
              "Shadow annotation can only be used on interfaces, but found one on %s (not an interface) pointing to %s",
              location.getName(), target));
    }

    transforms.put(target, meta);
  }

  @ClassTransform
  public void transform(ClassTransformContext classTransformContext) {
    CtClass transforming = classTransformContext.getCtClass();

    ClassMapping classMapping = this.classMappingProvider
        .get(transforming.getName());
    String name =
        classMapping != null ? classMapping.getDeobfuscatedName()
            : transforming.getName();

    if (!this.transforms.containsKey(name)) {
      return;
    }

    Collection<AnnotationMeta<Shadow>> metas = this.transforms.get(name);

    for (AnnotationMeta<Shadow> meta : metas) {
      CtClass ifc = meta.getClassIdentifier().getLocation();
      transforming.addInterface(ifc);

      this.transformMethods(meta, ifc, transforming);
    }
  }

  private void transformMethods(
      AnnotationMeta<Shadow> meta, CtClass ifc, CtClass transforming) {
    for (RegisteredShadowHandler<?> handler : this.service.getHandlers()) {
      for (AnnotationMeta subMeta : meta.getMetaData(handler.getAnnotationType())) {
        try {
          handler.getHandler().transform(ifc, transforming, subMeta);
        } catch (ClassTransformException e) {
          this.logger.trace(
              String.format("Failed to execute @%s from shadow interface %s on class %s",
                  handler.getAnnotationType().getSimpleName(),
                  ifc.getName(),
                  transforming.getName()),
              e);
        }
      }
    }
  }
}
