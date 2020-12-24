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

package net.flintmc.framework.config.internal.modifier;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.modifier.AnnotationModifier;
import net.flintmc.framework.config.modifier.ConfigModificationHandler;
import net.flintmc.framework.config.modifier.ConfigModifierRegistry;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

@Singleton
@Implement(ConfigModifierRegistry.class)
@Service(
    value = AnnotationModifier.class,
    priority = 100 /* needs to be called after anything with the configs */)
public class DefaultConfigModifierRegistry
    implements ConfigModifierRegistry, ServiceHandler<AnnotationModifier> {

  private final ConfigGenerator configGenerator;
  private final CtClass handlerType;
  private final Multimap<ConfigObjectReference, ConfigModificationHandler> handlers;

  @Inject
  private DefaultConfigModifierRegistry(ClassPool pool, ConfigGenerator configGenerator)
      throws NotFoundException {
    this.configGenerator = configGenerator;
    this.handlerType = pool.get(ConfigModificationHandler.class.getName());
    this.handlers = HashMultimap.create();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<ConfigModificationHandler> getHandlers() {
    return Collections.unmodifiableCollection(this.handlers.values());
  }

  /** {@inheritDoc} */
  @Override
  public <A extends Annotation> A modify(ConfigObjectReference reference, A annotation) {
    Collection<ConfigModificationHandler> handlers = this.handlers.get(reference);
    if (handlers.isEmpty()) {
      return annotation;
    }

    for (ConfigModificationHandler handler : handlers) {
      A modified = (A) handler.modify(annotation);
      if (modified != null) {
        annotation = modified;
      }
    }

    return annotation;
  }

  /** {@inheritDoc} */
  @Override
  public void discover(AnnotationMeta<AnnotationModifier> meta) throws ServiceNotFoundException {
    AnnotationModifier modifier = meta.getAnnotation();
    CtClass handler = (CtClass) meta.getIdentifier().getLocation();

    try {
      if (!handler.subtypeOf(this.handlerType)) {
        throw new ServiceNotFoundException(
            "Handler " + handler.getName() + " doesn't implement " + this.handlerType.getName());
      }
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException(
          "Failed to check for the sub type of " + handler.getName(), e);
    }

    for (ParsedConfig config : this.configGenerator.getDiscoveredConfigs()) {
      for (ConfigObjectReference reference : config.getConfigReferences()) {
        if (modifier.value().isAssignableFrom(reference.getDeclaringClass())
            && reference.getLastName().equals(modifier.method())) {
          this.handlers.put(
              reference, InjectionHolder.getInjectedInstance(CtResolver.get(handler)));
        }
      }
    }
  }
}
