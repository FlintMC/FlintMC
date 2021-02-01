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

package net.flintmc.transform.shadow.internal.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;

@Singleton
@Service(value = RegisterShadowHandler.class, priority = -25000, state = Service.State.AFTER_IMPLEMENT)
public class ShadowHandlerService implements ServiceHandler<RegisterShadowHandler> {

  private final List<RegisteredShadowHandler<?>> handlers;

  @Inject
  private ShadowHandlerService() {
    this.handlers = new ArrayList<>();
  }

  public List<RegisteredShadowHandler<?>> getHandlers() {
    return this.handlers;
  }

  @Override
  public void discover(AnnotationMeta<RegisterShadowHandler> meta) {
    ClassIdentifier identifier = meta.getClassIdentifier();
    RegisterShadowHandler annotation = meta.getAnnotation();
    ShadowHandler<?> handler =
        InjectionHolder.getInjectedInstance(CtResolver.get(identifier.getLocation()));

    this.handlers
        .add(new RegisteredShadowHandler(annotation.value(), annotation.priority(), handler));

    this.handlers.sort(Comparator.comparingInt(RegisteredShadowHandler::getPriority));
  }
}
