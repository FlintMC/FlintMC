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

package net.flintmc.mcapi.v1_15_2.resources.pack;

import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.pack.ResourcePack;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.resources.ResourcePackType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/** 1.15.2 implementation of a Flint resource pack */
public class VersionedResourcePack implements ResourcePack {
  private final ClientResourcePackInfo info;

  /**
   * Constructs a new {@link VersionedResourcePack} based on the given info.
   *
   * @param info Information about the resource pack
   */
  protected VersionedResourcePack(ClientResourcePackInfo info) {
    this.info = info;
  }

  /** {@inheritDoc} */
  public Collection<String> getNameSpaces() {
    return this.info.getResourcePack().getResourceNamespaces(ResourcePackType.CLIENT_RESOURCES);
  }

  /** {@inheritDoc} */
  public InputStream getStream(ResourceLocation resourceLocation) throws IOException {
    return this.info
        .getResourcePack()
        .getResourceStream(ResourcePackType.CLIENT_RESOURCES, resourceLocation.getHandle());
  }

  /** {@inheritDoc} */
  public String getName() {
    return info.getName();
  }

  /** {@inheritDoc} */
  public String getDescription() {
    return info.getDescription().getFormattedText();
  }

  /** {@inheritDoc} */
  public String getTitle() {
    return info.getTitle().getFormattedText();
  }
}
