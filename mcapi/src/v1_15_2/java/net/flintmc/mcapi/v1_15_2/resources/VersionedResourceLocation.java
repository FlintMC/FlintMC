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

package net.flintmc.mcapi.v1_15_2.resources;

import java.io.IOException;
import java.io.InputStream;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of a minecraft resource location.
 */
@Implement(value = ResourceLocation.class, version = "1.15.2")
public class VersionedResourceLocation extends net.minecraft.util.ResourceLocation
    implements ResourceLocation {

  @AssistedInject
  private VersionedResourceLocation(@Assisted("fullPath") String fullPath) {
    super(fullPath);
  }

  @AssistedInject
  private VersionedResourceLocation(
      @Assisted("nameSpace") String nameSpace, @Assisted("path") String path) {
    super(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <T> T getHandle() {
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  public InputStream openInputStream() throws IOException {
    return Minecraft.getInstance().getResourceManager().getResource(this).getInputStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exists() {
    return Minecraft.getInstance().getResourceManager().hasResource(this);
  }

  /**
   * Retrieves the resource location as a {@link String}.
   *
   * @return The resource location as a string.
   */
  @Override
  public String toString() {
    return this.namespace + ":" + this.path;
  }
}
