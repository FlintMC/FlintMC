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

package net.flintmc.mcapi.internal.settings.flint.serializer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.commons.javassist.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.metaprogramming.identifier.Identifier;

@Singleton
@Service(SettingsSerializer.class)
public class SettingsSerializerService implements ServiceHandler<SettingsSerializer> {

  private final JsonSettingsSerializer serializer;

  @Inject
  public SettingsSerializerService(JsonSettingsSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public void discover(AnnotationMeta<SettingsSerializer> meta) throws ServiceNotFoundException {
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass handlerType = identifier.getLocation();

    SettingsSerializationHandler handler =
        InjectionHolder.getInjectedInstance(CtResolver.get(handlerType));

    this.serializer.registerHandler(meta.getAnnotation().value(), handler);
  }
}
