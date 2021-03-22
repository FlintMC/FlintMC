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

package net.flintmc.util.classcache;

import java.util.Optional;
import java.util.function.Function;

/**
 * Utility to cache class bytecode on disk to skip class transformations or
 * bytecode generation in future startups.
 */
public interface ClassCache {

  /**
   * Retrieves the latest cached bytecode for a class with the given name.
   *
   * @param name the name of the class
   * @return empty, if no bytecode is cached, the bytecode if there is a cache
   * hit
   */
  Optional<byte[]> getClass(String name);

  /**
   * Retrieves the bytecode of a given class at a given cache ID.
   *
   * @param name    the name of the class
   * @param cacheId the identifier for the desired state of the bytecode
   * @return the bytecode if there was a cache hit, empty if not
   */
  Optional<byte[]> getClass(String name, long cacheId);

  /**
   * Retrieves the latest cached bytecode for a class with the given name.
   *
   * @param name     the name of the class
   * @param original the fallback bytecode if there is no cache hit
   * @return the cached bytecode or original if there is no cache hit
   */
  byte[] getClassOrDefault(String name, byte[] original);

  /**
   * Retrieves the bytecode of a given class at a given cache ID or writes
   * bytecode to that class cache.
   *
   * @param name     the name of the class
   * @param cacheId  the identifier for the desired state of the bytecode
   * @param original the fallback bytecode to write and return if no cache hit
   *                 occurs
   * @return the cached bytecode if the is a cache hit or original if not
   */
  byte[] getClassOrWrite(String name, long cacheId, byte[] original);

  /**
   * Writes bytecode to the cache of a given class at a given cache ID.
   *
   * @param name     the name of the class
   * @param cacheId  the identifier of the given state
   * @param byteCode the bytecode to write
   */
  void writeClass(String name, long cacheId, byte[] byteCode);

  /**
   * Retrieves cached bytecode for a given class at a given cache ID or runs a
   * given bytecode transformer and writes the result to the cache at the given
   * identifiers if no cache hit occured.
   *
   * @param name        the name of the class
   * @param cacheId     the identifier for the state of the bytecode
   * @param original    the original bytecode to be transformed if no cache hit
   *                    occurs
   * @param transformer the transformer that transforms the original bytecode
   *                    the desired state that should be cached
   * @return the transformed bytecode
   */
  byte[] getOrTransformAndWriteClass(String name, long cacheId, byte[] original,
      Function<byte[], byte[]> transformer);

}
