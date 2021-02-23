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
import java.util.ArrayList;
import java.util.Collection;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.Service.State;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service(value = ImplementedConfig.class, state = State.PRE_INIT, priority = -1000)
public class ImplementedConfigService implements ServiceHandler<ImplementedConfig> {

  private final Collection<String> configInterfaces;

  @Inject
  private ImplementedConfigService() {
    this.configInterfaces = new ArrayList<>();
  }

  public Collection<String> getConfigInterfaces() {
    return this.configInterfaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<ImplementedConfig> annotationMeta) {
    this.configInterfaces.add(annotationMeta.getClassIdentifier().getLocation().getName());
  }
}
