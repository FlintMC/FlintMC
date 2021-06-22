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

package net.flintmc.framework.config.internal.defval.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapper;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.commons.javassist.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.metaprogramming.AnnotationMeta;

@Singleton
@Implement(DefaultAnnotationMapperRegistry.class)
@Service(value = DefaultAnnotationMapper.class, priority = -50000)
public class DefaultAnnotationMapperRegistryService
    implements ServiceHandler<DefaultAnnotationMapper>, DefaultAnnotationMapperRegistry {

  private final Map<Class<? extends Annotation>, DefaultAnnotationMapperHandler<?>> mappers;

  @Inject
  private DefaultAnnotationMapperRegistryService() {
    this.mappers = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Class<? extends Annotation>> getAnnotationTypes() {
    return Collections.unmodifiableCollection(this.mappers.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Supplier<Object> getDefaultValue(ConfigObjectReference reference, Annotation annotation) {
    DefaultAnnotationMapperHandler handler = this.mappers.get(annotation.annotationType());
    return handler != null ? handler.getDefaultValue(reference, annotation) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<DefaultAnnotationMapper> meta) {
    this.mappers.put(
        meta.getAnnotation().value(),
        InjectionHolder.getInjectedInstance(
            CtResolver.get(meta.getClassIdentifier().getLocation())));
  }
}
