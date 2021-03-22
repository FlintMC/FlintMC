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

package net.flintmc.util.mojang.internal.cache;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.util.mojang.internal.cache.object.CacheIO;
import net.flintmc.util.mojang.internal.cache.object.CachedObjectIO;

@Singleton
@Service(CacheIO.class)
public class FileCacheService implements ServiceHandler<CacheIO> {

  private final FileCache cache;

  @Inject
  private FileCacheService(FileCache cache) {
    this.cache = cache;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void discover(AnnotationMeta<CacheIO> meta) {
    CachedObjectIO io =
        InjectionHolder.getInjectedInstance(
            CtResolver.get(meta.getClassIdentifier().getLocation()));
    this.cache.registerIO(io.getType(), io);
  }
}
