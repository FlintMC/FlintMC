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

package net.flintmc.framework.config.internal.generator.service;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.binder.AnnotatedBindingBuilder;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.SingletonConfigHolder;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.annotation.MultiInstanceConfig;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.Identifier;

@Singleton
@Service(value = Config.class, priority = -100)
public class ConfigGenerationService implements ServiceHandler<Config> {

  private final ConfigGenerator generator;
  private final SingletonConfigHolder configHolder;

  @Inject
  private ConfigGenerationService(ConfigGenerator generator, SingletonConfigHolder configHolder) {
    this.generator = generator;
    this.configHolder = configHolder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<Config> meta) throws ServiceNotFoundException {
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass location = identifier.getLocation();

    try {
      Class<? extends ParsedConfig> config = this.generator.generateConfigImplementation(location);

      if (config == null) {
        return;
      }

      Class<?> base = CtResolver.get(location);

      InjectionHolder.getInstance()
          .addModules(
              new AbstractModule() {
                @Override
                protected void configure() {
                  AnnotatedBindingBuilder builder = super.bind((Class) base);

                  if (!location.hasAnnotation(MultiInstanceConfig.class)) {
                    ParsedConfig singleton = generator.createConfigInstance(config, true);
                    configHolder.registerSingletonConfig(singleton);
                    builder.toInstance(singleton);
                  } else {
                    builder.toProvider(() -> generator.createConfigInstance(config, true));
                  }
                }
              });

    } catch (NotFoundException | CannotCompileException
        | IOException | ReflectiveOperationException e) {
      throw new ServiceNotFoundException("Cannot generate config for " + location.getName(), e);
    }
  }
}
