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

package net.flintmc.util.classcache.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.classcache.CachedClass;
import org.apache.logging.log4j.Logger;

// This manual implementation of the Factory is necessary because the
// ClassCache is used before assisted factories are being made available by the
// framework.
@Singleton
@Implement(CachedClass.Factory.class)
public class DefaultCachedClassFactory implements CachedClass.Factory {

  private final Logger logger;

  @Inject
  private DefaultCachedClassFactory(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CachedClass create(String name, UUID uuid) {
    return new DefaultCachedClass(name, uuid, logger);
  }
}
