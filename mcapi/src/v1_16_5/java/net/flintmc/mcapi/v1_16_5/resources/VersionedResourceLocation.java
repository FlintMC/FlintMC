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

package net.flintmc.mcapi.v1_16_5.resources;

import java.io.IOException;
import java.io.InputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

/**
 * 1.16.5 implementation of a minecraft resource location.
 */
@Implement(ResourceLocation.class)
public class VersionedResourceLocation implements ResourceLocation {

  private final net.minecraft.util.ResourceLocation wrapped;

  @AssistedInject
  private VersionedResourceLocation(@Assisted("fullPath") String fullPath) {
    this.wrapped = new net.minecraft.util.ResourceLocation(fullPath);
  }

  @AssistedInject
  private VersionedResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path) {
    this.wrapped = new net.minecraft.util.ResourceLocation(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this.wrapped;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPath() {
    return this.wrapped.getPath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNamespace() {
    return this.wrapped.getNamespace();
  }

  /**
   * {@inheritDoc}
   */
  public InputStream openInputStream() throws IOException {
    return Minecraft.getInstance().getResourceManager().getResource(this.wrapped).getInputStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exists() {
    return Minecraft.getInstance().getResourceManager().hasResource(this.wrapped);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getNamespace() + ":" + this.getPath();
  }
}
