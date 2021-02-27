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

package net.flintmc.framework.inject.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtMethod;
import net.flintmc.framework.inject.FlintInitialize;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.processing.autoload.AnnotationMeta;

@Singleton
@Service(value = FlintInitialize.class, priority = 15_000_000)
public class FlintInitializeService implements ServiceHandler<FlintInitialize> {

  private final MethodInjector.Factory injectorFactory;

  @Inject
  private FlintInitializeService(MethodInjector.Factory injectorFactory) {
    this.injectorFactory = injectorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<FlintInitialize> meta) {
    CtMethod method = meta.getMethodIdentifier().getLocation();
    this.injectorFactory.generate(method, FlintInitializeInjector.class).invoke();
  }

  public interface FlintInitializeInjector {

    void invoke();
  }
}
