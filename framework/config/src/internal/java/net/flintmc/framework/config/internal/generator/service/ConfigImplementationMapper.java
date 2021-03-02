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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.HashMap;
import java.util.Map;
import javassist.CtClass;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.Service.State;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service(value = ConfigImplementation.class, state = State.PRE_INIT, priority = -1)
public class ConfigImplementationMapper implements ServiceHandler<ConfigImplementation> {

  private final Map<String, CtClass> implementationMappings;
  private final Map<String, String> launchArguments;

  @Inject
  private ConfigImplementationMapper(@Named("launchArguments") Map launchArguments) {
    this.implementationMappings = new HashMap<>();
    this.launchArguments = launchArguments;
  }

  public Map<String, CtClass> getImplementationMappings() {
    return this.implementationMappings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<ConfigImplementation> annotationMeta) {
    ConfigImplementation annotation = annotationMeta.getAnnotation();

    String version = annotation.version();

    if (!version.isEmpty() && !launchArguments.get("--game-version").equals(version)) {
      return;
    }

    String ifc = annotation.value().getName();

    this.implementationMappings.put(ifc, annotationMeta.getClassIdentifier().getLocation());
  }
}
