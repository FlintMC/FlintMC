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

package net.flintmc.util.mappings.utils.line;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.Service.State;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandler;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandlerRegistry;
import net.flintmc.util.mappings.utils.line.handler.LineObfuscationMapper;

@Singleton
@Service(value = LineObfuscationMapper.class, priority = -9999999, state = State.PRE_INIT)
public class LineMapperService implements ServiceHandler<LineObfuscationMapper> {

  private final LineMappingHandlerRegistry registry;

  @Inject
  private LineMapperService(LineMappingHandlerRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void discover(AnnotationMeta<LineObfuscationMapper> meta) {
    LineMappingHandler handler = InjectionHolder.getInjectedInstance(
        CtResolver.get(meta.getClassIdentifier().getLocation()));
    this.registry.registerHandler(handler, meta.getAnnotation().value());
  }
}
