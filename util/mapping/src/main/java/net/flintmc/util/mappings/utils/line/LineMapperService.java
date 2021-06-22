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
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandler;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandlerRegistry;
import net.flintmc.util.mappings.utils.line.handler.LineObfuscationMapper;

@Singleton
//@Service(value = LineObfuscationMapper.class, priority = -9999999, state = State.PRE_INIT)
public class LineMapperService /*implements ServiceHandler<LineObfuscationMapper>*/ {

  private final ClassLineMappingHandler classLineMappingHandler;
  private final MethodLineMappingHandler methodLineMappingHandler;
  private final FieldLineMappingHandler fieldLineMappingHandler;

  @Inject
  private LineMapperService(
      ClassLineMappingHandler classLineMappingHandler,
      MethodLineMappingHandler methodLineMappingHandler,
      FieldLineMappingHandler fieldLineMappingHandler) {
    this.classLineMappingHandler = classLineMappingHandler;
    this.methodLineMappingHandler = methodLineMappingHandler;
    this.fieldLineMappingHandler = fieldLineMappingHandler;
  }

  private void register(LineMappingHandler lineMappingHandler, LineMappingHandlerRegistry registry) {
    registry.registerHandler(lineMappingHandler, lineMappingHandler.getClass().getDeclaredAnnotation(LineObfuscationMapper.class).value());
  }

  public void install(LineMappingHandlerRegistry registry) {
    this.register(classLineMappingHandler, registry);
    this.register(fieldLineMappingHandler, registry);
    this.register(methodLineMappingHandler, registry);
  }

//  @Override
  /*public void discover(AnnotationMeta<LineObfuscationMapper> meta) {
    LineMappingHandler handler = InjectionHolder.getInjectedInstance(
        CtResolver.get(meta.getClassIdentifier().getLocation()));
    this.registry.registerHandler(handler, meta.getAnnotation().value());
  }*/
}
