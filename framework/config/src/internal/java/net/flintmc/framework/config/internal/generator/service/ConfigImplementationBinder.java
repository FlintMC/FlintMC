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
import java.util.ArrayList;
import java.util.Collection;
import javassist.CtClass;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.internal.transform.ConfigTransformer;
import net.flintmc.framework.config.internal.transform.TransformedConfigMeta;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;

@Singleton
@Service(
    value = ConfigImplementation.class,
    priority = 3 /* needs to be called after the ConfigTransformer */)
public class ConfigImplementationBinder implements ServiceHandler<ConfigImplementation> {

  private final ConfigGenerator configGenerator;
  private final ConfigTransformer transformer;

  @Inject
  private ConfigImplementationBinder(
      ConfigGenerator configGenerator, ConfigTransformer transformer) {
    this.configGenerator = configGenerator;
    this.transformer = transformer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void discover(AnnotationMeta<ConfigImplementation> annotationMeta)
      throws ServiceNotFoundException {
    // flush the implemented configs from the ConfigTransformer
    // into the injector

    for (TransformedConfigMeta meta : this.transformer.getMappings()) {
      try {
        CtClass implementation = meta.getImplementationCtClass();

        // load the class so that the transformer will be called
        Class<?> definedImplementation =
            super.getClass().getClassLoader().loadClass(implementation.getName());
        meta.setImplementationClass(definedImplementation);
      } catch (ClassNotFoundException exception) {
        throw new ServiceNotFoundException("Cannot load transformed config class", exception);
      }
    }

    Collection<TransformedConfigMeta> mappings = this.transformer.getMappings();
    if (mappings.isEmpty()) {
      return;
    }

    Collection<TransformedConfigMeta> pendingConfigs = new ArrayList<>();

    InjectionHolder.getInstance()
        .addModules(
            new AbstractModule() {
              @Override
              protected void configure() {
                for (TransformedConfigMeta meta : mappings) {
                  Class<?> superClass = meta.getSuperClass();
                  Class<?> implementation = meta.getImplementationClass();

                  super.bind((Class) superClass).to(implementation);

                  if (meta.getImplementationCtClass().getName().equals(implementation.getName())) {
                    pendingConfigs.add(meta);
                  }
                }

                mappings.clear();
              }
            });

    // load the module so that the pendingConfigs will be filled
    InjectionHolder.getInstance().getInjector();

    // register the configs in the ConfigGenerator after the module has been configured
    for (TransformedConfigMeta meta : pendingConfigs) {
      ParsedConfig config =
          (ParsedConfig) InjectionHolder.getInjectedInstance(meta.getSuperClass());
      CtClass base =
          meta.getConfig().getGeneratedImplementation(meta.getConfig().getBaseClass().getName());
      if (!base.getName().equals(config.getClass().getName())) {
        continue;
      }

      this.configGenerator.bindConfig(meta.getConfig(), config);
    }
  }
}
