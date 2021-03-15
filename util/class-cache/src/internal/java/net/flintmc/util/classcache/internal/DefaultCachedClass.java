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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.util.classcache.CachedClass;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Implement(CachedClass.class)
public class DefaultCachedClass implements CachedClass {

  private final Logger logger;

  private final String name;
  private final UUID uuid;

  private final File file;

  DefaultCachedClass(@Assisted("name") String name,
      @Assisted("uuid") UUID uuid, @InjectLogger Logger logger) {
    this.logger = logger;
    this.name = name;
    this.uuid = uuid;
    this.file = new File(
        CachedClass.CACHED_CLASS_PATH.replace("{UUID}", this.uuid.toString()));
    //noinspection ResultOfMethodCallIgnored
    this.file.getParentFile().mkdirs();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] read() {
    try (FileInputStream in = new FileInputStream(this.file)) {

      // we never write files longer than Integer.MAX_VALUE
      byte[] bytes = new byte[(int) this.file.length()];

      int offset = 0;
      int read;

      while ((read = in.read(bytes, offset, bytes.length - offset)) > 0) {
        offset += read;
      }

      return bytes;
    } catch (IOException e) {
      this.logger
          .error("Failed to read cached class {} from {}.", this.getName(),
              this.file.getName(), e);
    }
    // return null instead of empty error to cause exceptions if this fails
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(byte[] bytecode) {
    try (FileOutputStream out = new FileOutputStream(this.file, false)) {
      out.write(bytecode);
    } catch (IOException e) {
      this.logger.error("Failed to write class cache for class {} to {}",
          this.getName(), this.file.getName(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUUID() {
    return this.uuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBytecode() {
    return this.file.exists();
  }
}
