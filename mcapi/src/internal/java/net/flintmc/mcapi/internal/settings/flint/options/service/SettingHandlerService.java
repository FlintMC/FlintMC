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

package net.flintmc.mcapi.internal.settings.flint.options.service;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javassist.CtClass;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.Identifier;

@Singleton
@Implement(SettingHandler.class)
@Service(
    value = RegisterSettingHandler.class,
    priority = -1 /* load before the SettingsDiscoverer */)
public class SettingHandlerService
    implements SettingHandler<Annotation>, ServiceHandler<RegisterSettingHandler> {

  private final Map<Class<? extends Annotation>, SettingHandler> handlers;
  private final Map<Class<? extends Annotation>, CtClass> pendingHandlers;

  public SettingHandlerService() {
    this.handlers = new HashMap<>();
    this.pendingHandlers = new ConcurrentHashMap<>();
  }

  @Override
  public JsonObject serialize(
      Annotation annotation, RegisteredSetting setting, Object currentValue) {
    SettingHandler<Annotation> handler = this.getHandler(annotation);
    return handler == null
        ? new JsonObject()
        : handler.serialize(annotation, setting, currentValue);
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, Annotation annotation) {
    SettingHandler<Annotation> handler = this.getHandler(annotation);
    return handler == null || handler.isValidInput(input, reference, annotation);
  }

  private void processPendingHandlers() {
    if (this.pendingHandlers.isEmpty()) {
      return;
    }

    this.pendingHandlers.forEach(
        (annotationType, type) ->
            this.handlers.put(
                annotationType, InjectionHolder.getInjectedInstance(CtResolver.get(type))));

    this.pendingHandlers.clear();
  }

  @Override
  public void discover(AnnotationMeta<RegisterSettingHandler> meta)
      throws ServiceNotFoundException {
    Class<? extends Annotation> annotationType = meta.getAnnotation().value();
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass type = identifier.getLocation();

    if (!annotationType.isAnnotationPresent(ApplicableSetting.class)) {
      throw new ServiceNotFoundException(
          "Invalid SettingHandler registered for handler "
              + type.getName()
              + ": The annotation "
              + annotationType.getName()
              + " is not annotated with "
              + ApplicableSetting.class.getName());
    }

    if (this.pendingHandlers.containsKey(annotationType)) {
      throw new ServiceNotFoundException(
          "Failed to register SettingHandler "
              + type.getName()
              + ": Cannot register multiple handlers for the annotation "
              + annotationType.getName());
    }

    this.pendingHandlers.put(annotationType, type);
  }

  private <A extends Annotation> SettingHandler<A> getHandler(A annotation) {
    this.processPendingHandlers();

    return this.handlers.get(annotation.annotationType());
  }
}
