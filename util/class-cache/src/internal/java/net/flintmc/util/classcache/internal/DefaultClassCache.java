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
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.classcache.CachedClass;
import net.flintmc.util.classcache.ClassCache;
import net.flintmc.util.classcache.ClassCacheIndex;

import java.util.Optional;
import java.util.function.Function;

@Singleton
@Implement(ClassCache.class)
public class DefaultClassCache implements ClassCache {

  private final ClassCacheIndex index;

  @Inject
  private DefaultClassCache(ClassCacheIndex index) {
    this.index = index;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<byte[]> getClass(String name) {
    long latest = this.index.getLatestId(name);
    return getClass(name, latest);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<byte[]> getClass(String name, long cacheId) {
    CachedClass cachedClass = this.index.getCachedClass(name, cacheId);
    if (cachedClass.hasBytecode()) {
      return Optional.of(cachedClass.read());
    } else {
      return Optional.empty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getClassOrDefault(String name, byte[] original) {
    return this.getClass(name).orElse(original);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getClassOrWrite(String name, long cacheId, byte[] original) {
    Optional<byte[]> bytecode = this.getClass(name, cacheId);
    if (bytecode.isPresent()) {
      return bytecode.get();
    } else {
      this.writeClass(name, cacheId, original);
      return original;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeClass(String name, long cacheId, byte[] byteCode) {
    CachedClass cachedClass = this.index.getCachedClass(name, cacheId);
    cachedClass.write(byteCode);
    this.index.write();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getOrTransformAndWriteClass(String name, long cacheId,
      byte[] original, Function<byte[], byte[]> transformer) {
    Optional<byte[]> cached = this.getClass(name, cacheId);
    if (cached.isPresent()) {
      return cached.get();
    } else {
      byte[] bytecode = transformer.apply(original);
      this.writeClass(name, cacheId, bytecode);
      return bytecode;
    }
  }

}
